package io.github.toberocat.improvedfactions.database

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.jetbrains.exposed.sql.Database
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

enum class DatabaseType {
    H2 {
        override fun connect(plugin: ImprovedFactionsPlugin) = Database.connect(
            "jdbc:h2:file:${plugin.dataFolder.absolutePath}/database.h2", driver = "org.h2.Driver"
        )
    },
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
            val password = config.getString("mysql.password") ?: "1234"
            val port = config.getInt("mysql.port")

            if (isMySQLServerReachable(host, port, user, password)) {
                try {
                    val database = Database.connect(
                        "jdbc:mysql://$host:$port/$db",
                        driver = "com.mysql.cj.jdbc.Driver",
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
                Class.forName("com.mysql.cj.jdbc.Driver")
                val url = "jdbc:mysql://$host:$port"
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