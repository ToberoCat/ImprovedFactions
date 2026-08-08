package io.github.toberocat.improvedfactions.database

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.utils.getEnum
import org.flywaydb.core.Flyway

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
                    location = SQLITE_LOCATION
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
                    location = MYSQL_LOCATION
                )
            }
        }
    }

    internal fun migrate(
        jdbcUrl: String,
        location: String,
        user: String? = null,
        password: String? = null
    ) {
        Flyway.configure()
            .dataSource(jdbcUrl, user ?: "", password ?: "")
            .locations(location)
            .baselineOnMigrate(true)
            .load()
            .migrate()
    }
}
