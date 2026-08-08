package io.github.toberocat.improvedfactions.database.storage

import java.sql.Connection
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import javax.sql.DataSource

/** The only runtime JDBC write boundary. Commands must contain copied scalar/DTO input only. */
class JdbcStorageRepository(
    private val dataSource: DataSource,
    private val dispatcher: StorageDispatcher,
    private val snapshots: ClaimRepository
) {
    fun <T> read(query: (Connection) -> T): CompletionStage<T> = dispatcher.submit {
        dataSource.connection.use { connection -> query(connection) }
    }

    fun <T> write(command: (Connection) -> T): CompletionStage<T> {
        val committed = dispatcher.submit {
            dataSource.connection.use { connection ->
                connection.autoCommit = false
                try {
                    val result = command(connection)
                    connection.commit()
                    result
                } catch (failure: Throwable) {
                    runCatching { connection.rollback() }
                    throw failure
                }
            }
        }
        return committed.thenCompose { result ->
            snapshots.refresh().thenApply { result }
        }
    }
}

class MainThreadContinuation(private val schedule: (Runnable) -> Unit) {
    fun execute(action: () -> Unit) = schedule(Runnable(action))

    fun <T> resume(
        stage: CompletionStage<T>,
        success: (T) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        stage.whenComplete { value, throwable ->
            schedule(Runnable {
                if (throwable == null) success(value) else failure(unwrap(throwable))
            })
        }
    }

    private fun unwrap(throwable: Throwable): Throwable = throwable.cause ?: throwable
}
