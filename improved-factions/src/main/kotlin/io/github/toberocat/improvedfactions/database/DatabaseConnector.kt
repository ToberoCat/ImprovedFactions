package io.github.toberocat.improvedfactions.database

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.initializeDatabase
import io.github.toberocat.improvedfactions.database.DatabaseManager.verboseLogging
import io.github.toberocat.improvedfactions.database.storage.StorageManager
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
        val settings = DatabaseSettings.from(plugin)
        logger.info("Using database ${settings.type} as database")

        DatabaseMigrator.migrate(plugin)
        val dataSource = createDataSource(settings)
        val database = Database.connect(dataSource)

        if (config.getBoolean("verbose-database-logging")) {
            verboseLogging = true
        }
        initializeDatabase()
        StorageManager.start(settings, dataSource, logger) { continuation ->
            plugin.server.scheduler.runTask(plugin, continuation)
        }
        return database
    }
}
