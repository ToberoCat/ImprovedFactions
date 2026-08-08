package io.github.toberocat.improvedfactions.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.utils.getEnum

data class DatabaseSettings(
    val type: DatabaseType,
    val jdbcUrl: String,
    val user: String?,
    val password: String?,
    val mysqlMaximumPoolSize: Int
) {
    companion object {
        fun from(plugin: ImprovedFactionsPlugin): DatabaseSettings {
            val type = plugin.config.getEnum<DatabaseType>("database") ?: DatabaseType.SQLITE
            return when (type) {
                DatabaseType.SQLITE -> DatabaseSettings(
                    type,
                    "jdbc:sqlite:${plugin.dataFolder.absolutePath}/database.sqlite",
                    null,
                    null,
                    1
                )

                DatabaseType.MYSQL -> {
                    val config = plugin.config
                    val host = config.getString("mysql.host") ?: "localhost"
                    val port = config.getInt("mysql.port", 3306)
                    val database = config.getString("mysql.database") ?: "improvedfactions"
                    DatabaseSettings(
                        type,
                        "jdbc:mariadb://$host:$port/$database",
                        config.getString("mysql.user") ?: "root",
                        config.getString("mysql.password") ?: "",
                        config.getInt("mysql.maximum-pool-size", 4).coerceIn(1, 16)
                    )
                }
            }
        }
    }
}

fun createDataSource(settings: DatabaseSettings): HikariDataSource {
    val config = HikariConfig().apply {
        poolName = "ImprovedFactions-${settings.type.name.lowercase()}"
        jdbcUrl = settings.jdbcUrl
        username = settings.user
        password = settings.password
        maximumPoolSize = when (settings.type) {
            DatabaseType.SQLITE -> 1
            DatabaseType.MYSQL -> settings.mysqlMaximumPoolSize.coerceIn(1, 16)
        }
        minimumIdle = 0
        connectionTimeout = 10_000
        validationTimeout = 3_000
        initializationFailTimeout = -1
        isAutoCommit = false
        driverClassName = when (settings.type) {
            DatabaseType.SQLITE -> "org.sqlite.JDBC"
            DatabaseType.MYSQL -> "org.mariadb.jdbc.Driver"
        }
        if (settings.type == DatabaseType.SQLITE) {
            addDataSourceProperty("busy_timeout", "10000")
            addDataSourceProperty("journal_mode", "WAL")
        }
    }
    return HikariDataSource(config)
}
