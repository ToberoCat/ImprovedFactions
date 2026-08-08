package io.github.toberocat.improvedfactions.database

import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.improvedfactions.invites.FactionInvites
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
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
            Factions.handleQueues()
            FactionRankHandler.initRanks()
            FactionInvites.scheduleInviteExpirations()
        }
    }
}
