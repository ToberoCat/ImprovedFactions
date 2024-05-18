package io.github.toberocat.improvedfactions.database

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

    fun createTables(vararg tables: Table) =
        SchemaUtils.createMissingTablesAndColumns(*tables, withLogs = verboseLogging)
}