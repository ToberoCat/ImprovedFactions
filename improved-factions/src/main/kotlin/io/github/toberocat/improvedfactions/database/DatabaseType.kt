package io.github.toberocat.improvedfactions.database

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.jetbrains.exposed.sql.Database
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

enum class DatabaseType {
    SQLITE {
        override fun connect(plugin: ImprovedFactionsPlugin) = Database.connect(
            "jdbc:sqlite:${plugin.dataFolder.absolutePath}/database.sqlite", driver = "org.sqlite.JDBC"
        )
    },
    MYSQL {
        override fun connect(plugin: ImprovedFactionsPlugin): Database? {
            val config = plugin.config
            val logger = plugin.logger

            val host = config.getString("mysql.host")
            val db = config.getString("mysql.database")
            val user = config.getString("mysql.user") ?: "root"
            val password = config.getString("mysql.password") ?: ""
            val port = config.getInt("mysql.port")

            if (isMySQLServerReachable(host, port, user, password)) {
                try {
                    val database = Database.connect(
                        "jdbc:mariadb://$host:$port/$db",
                        driver = "org.mariadb.jdbc.Driver",
                        user = user,
                        password = password
                    )

                    logger.info("Successfully connected to MySQL")
                    return database
                } catch (e: SQLException) {
                    logger.warning("Failed to connect to MySQL: ${e.message}")
                }
            } else {
                throw IllegalArgumentException("MySqL server not reachable. Check credentials")
            }
            return null
        }

        private fun isMySQLServerReachable(
            host: String?,
            port: Int,
            username: String,
            password: String
        ): Boolean {
            var connection: Connection? = null

            return try {
                Class.forName("org.mariadb.jdbc.Driver")
                val url = "jdbc:mariadb://$host:$port"
                connection = DriverManager.getConnection(url, username, password)
                val statement: Statement = connection.createStatement()
                val resultSet = statement.executeQuery("SELECT 1")
                resultSet.next()
            } catch (e: SQLException) {
                false
            } finally {
                connection?.close()
            }
        }
    };

    abstract fun connect(plugin: ImprovedFactionsPlugin): Database?
}
