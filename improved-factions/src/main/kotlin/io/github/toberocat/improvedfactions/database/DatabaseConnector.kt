package io.github.toberocat.improvedfactions.database

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.initializeDatabase
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.database.DatabaseManager.verboseLogging
import io.github.toberocat.improvedfactions.utils.getEnum
import org.bukkit.configuration.file.FileConfiguration
import org.jetbrains.exposed.sql.Database
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

        if (config.getBoolean("verbose-database-logging")) {
            verboseLogging = true
        }
        initializeDatabase()
        return database
    }

    private fun connectDatabase(): Database {
        val databaseType = config.getEnum<DatabaseType>("database").let {
            if (it != null)
                return@let it
            logger.warning("No database specified. Using sqlite as default")
            return@let DatabaseType.SQLITE
        }

        logger.info("Using database $databaseType as database")
        return databaseType.connect(plugin) ?: throw IllegalArgumentException("Database connection failed")
    }
}