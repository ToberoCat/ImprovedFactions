package io.github.toberocat.improvedfactions.database

import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.improvedfactions.factions.ban.FactionBans
import io.github.toberocat.improvedfactions.invites.FactionInvites
import io.github.toberocat.improvedfactions.permissions.FactionPermissions
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import io.github.toberocat.improvedfactions.ranks.FactionRanks
import io.github.toberocat.improvedfactions.user.FactionUsers
import io.github.toberocat.improvedfactions.utils.offline.KnownOfflinePlayers
import io.github.toberocat.improvedfactions.utils.options.limit.PlayerUsageLimits
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseManager {

    var verboseLogging: Boolean = false

    inline fun <T> loggedTransaction(crossinline statement: Transaction.() -> T): T = transaction {
        if (verboseLogging) {
            addLogger(StdOutSqlLogger)
        }

        statement()
    }

    fun initializeDatabase() {
        loggedTransaction {
            createTables(
                FactionUsers,
                FactionClaims,
                FactionPermissions,
                FactionBans,
                PlayerUsageLimits,
                Factions,
                FactionRanks,
                FactionInvites,
                KnownOfflinePlayers
            )

            Factions.handleQueues()
            FactionRankHandler.initRanks()
            FactionInvites.scheduleInviteExpirations()
        }
    }

    fun createTables(vararg tables: Table) =
        SchemaUtils.createMissingTablesAndColumns(*tables, withLogs = verboseLogging)
}