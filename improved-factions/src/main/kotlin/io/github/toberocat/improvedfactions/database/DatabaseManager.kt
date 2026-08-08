package io.github.toberocat.improvedfactions.database

import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.improvedfactions.invites.FactionInvites
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.statements.StatementInterceptor
import org.jetbrains.exposed.sql.transactions.TransactionManager
import io.github.toberocat.improvedfactions.database.storage.StorageManager

object DatabaseManager {

    var verboseLogging: Boolean = false

    inline fun <T> loggedTransaction(crossinline statement: Transaction.() -> T): T = transaction {
        if (verboseLogging) {
            addLogger(StdOutSqlLogger)
        }

        statement()
    }

    fun refreshStorageCacheAfterCommit() {
        val transaction = TransactionManager.currentOrNull() ?: run {
            StorageManager.invalidateAndRefresh()
            return
        }
        transaction.registerInterceptor(object : StatementInterceptor {
            override fun afterCommit(transaction: Transaction) {
                StorageManager.invalidateAndRefresh()
            }
        })
    }

    fun initializeDatabase() {
        loggedTransaction {
            FactionRankHandler.initRanks()
        }
    }
}
