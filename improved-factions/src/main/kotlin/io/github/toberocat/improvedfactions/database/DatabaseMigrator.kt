package io.github.toberocat.improvedfactions.database

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.utils.getEnum
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult
import java.util.logging.Level
import java.util.logging.Logger

/** Runs versioned schema migrations before Exposed starts using the database. */
object DatabaseMigrator {
    private const val SQLITE_LOCATION = "classpath:db/migration/sqlite"
    private const val MYSQL_LOCATION = "classpath:db/migration/mysql"

    fun migrate(plugin: ImprovedFactionsPlugin) {
        val type = plugin.config.getEnum<DatabaseType>("database") ?: DatabaseType.SQLITE
        when (type) {
            DatabaseType.SQLITE -> {
                migrate(
                    jdbcUrl = "jdbc:sqlite:${plugin.dataFolder.absolutePath}/database.sqlite",
                    location = SQLITE_LOCATION,
                    logger = plugin.logger
                )
            }

            DatabaseType.MYSQL -> {
                val config = plugin.config
                val host = config.getString("mysql.host") ?: "localhost"
                val port = config.getInt("mysql.port", 3306)
                val database = config.getString("mysql.database") ?: "improvedfactions"
                val user = config.getString("mysql.user") ?: "root"
                val password = config.getString("mysql.password") ?: ""
                migrate(
                    jdbcUrl = "jdbc:mariadb://$host:$port/$database",
                    user = user,
                    password = password,
                    location = MYSQL_LOCATION,
                    logger = plugin.logger
                )
            }
        }
    }

    internal fun migrate(
        jdbcUrl: String,
        location: String,
        user: String? = null,
        password: String? = null,
        logger: Logger? = null
    ): MigrateResult {
        logger?.info("[Flyway] Starting migrations: location=$location, database=${jdbcUrl.substringBefore('?')}")
        val flyway = Flyway.configure()
            .dataSource(jdbcUrl, user ?: "", password ?: "")
            .locations(location)
            .baselineOnMigrate(true)
            .load()

        val pending = flyway.info().pending()
        if (pending.isEmpty()) {
            logger?.info("[Flyway] No pending migrations")
        } else {
            pending.forEach { migration ->
                logger?.info(
                    "[Flyway] Applying ${migration.version}: ${migration.description} " +
                        "(${migration.script})"
                )
            }
        }

        return try {
            flyway.migrate().also { result ->
                result.getSuccessfulMigrations().forEach { migration ->
                    logger?.info(
                        "[Flyway] Applied ${migration.version ?: "<repeatable>"}: ${migration.description} " +
                            "(${migration.filepath}) in ${migration.executionTime} ms"
                    )
                }
                result.warnings.orEmpty().forEach { warning ->
                    logger?.warning("[Flyway] Warning: $warning")
                }
                logger?.info(
                    "[Flyway] Migration run completed: ${result.migrationsExecuted} migration(s), " +
                        "success=${result.success}"
                )
            }
        } catch (failure: Throwable) {
            logger?.log(Level.SEVERE, "[Flyway] Migration run failed", failure)
            throw failure
        }
    }
}
