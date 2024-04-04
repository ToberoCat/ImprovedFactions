package io.github.toberocat.improvedfactions

import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.improvedfactions.factions.ban.FactionBans
import io.github.toberocat.improvedfactions.invites.FactionInvites
import io.github.toberocat.improvedfactions.permissions.FactionPermissions
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import io.github.toberocat.improvedfactions.ranks.FactionRanks
import io.github.toberocat.improvedfactions.user.FactionUsers
import io.github.toberocat.improvedfactions.utils.options.limit.PlayerUsageLimits
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.logging.Logger

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
class DatabaseConnector(private val plugin: ImprovedFactionsPlugin) {
    private val config: FileConfiguration = plugin.config
    private val logger: Logger = plugin.logger

    fun createDatabase(): Database {
        val database = connectDatabase()
        transaction {
            if (config.getBoolean("mysql.verbose", true)) addLogger(StdOutSqlLogger)

            SchemaUtils.createMissingTablesAndColumns(FactionUsers)
            SchemaUtils.createMissingTablesAndColumns(FactionClaims)
            SchemaUtils.createMissingTablesAndColumns(FactionPermissions)
            SchemaUtils.createMissingTablesAndColumns(FactionBans)
            SchemaUtils.createMissingTablesAndColumns(PlayerUsageLimits)

            SchemaUtils.createMissingTablesAndColumns(Factions)
            Factions.handleQueues()

            SchemaUtils.createMissingTablesAndColumns(FactionRanks)
            FactionRankHandler.initRanks()

            SchemaUtils.createMissingTablesAndColumns(FactionInvites)
            FactionInvites.scheduleInviteExpirations()

            ImprovedFactionsPlugin.getActiveModules().forEach { (_, module) -> module.onLoadDatabase(plugin) }
        }
        return database
    }

    private fun connectDatabase(): Database {
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
        }
        logger.warning("Connection to MySQL server not possible. Falling back to SQLite...")
        return Database.connect(
            "jdbc:sqlite:${plugin.dataFolder.absolutePath}/database.sqlite", driver = "org.sqlite.JDBC"
        )
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
}