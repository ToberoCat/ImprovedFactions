package io.github.toberocat.improvedfactions.database

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.database.DatabaseManager.createTables
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.database.DatabaseManager.verboseLogging
import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.improvedfactions.factions.ban.FactionBans
import io.github.toberocat.improvedfactions.invites.FactionInvites
import io.github.toberocat.improvedfactions.permissions.FactionPermissions
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import io.github.toberocat.improvedfactions.ranks.FactionRanks
import io.github.toberocat.improvedfactions.user.FactionUsers
import io.github.toberocat.improvedfactions.utils.getEnum
import io.github.toberocat.improvedfactions.utils.options.limit.PlayerUsageLimits
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

        loggedTransaction {
            createTables(
                FactionUsers,
                FactionClaims,
                FactionPermissions,
                FactionBans,
                PlayerUsageLimits,
                Factions,
                FactionRanks,
                FactionInvites
            )

            Factions.handleQueues()
            FactionRankHandler.initRanks()
            FactionInvites.scheduleInviteExpirations()

            ImprovedFactionsPlugin.instance.moduleManager.initializeModuleDatabase()
        }
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