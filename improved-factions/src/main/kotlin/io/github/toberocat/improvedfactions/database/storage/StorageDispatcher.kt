package io.github.toberocat.improvedfactions.database.storage

import io.github.toberocat.improvedfactions.database.DatabaseType
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class StorageDispatcher private constructor(parallelism: Int) : AutoCloseable {
    private val closed = AtomicBoolean()
    private val executor = ThreadPoolExecutor(
        parallelism,
        parallelism,
        0L,
        TimeUnit.MILLISECONDS,
        java.util.concurrent.LinkedBlockingQueue(1024),
        StorageThreadFactory()
    )

    fun <T> submit(task: () -> T): CompletionStage<T> {
        if (closed.get()) throw RejectedExecutionException("Storage dispatcher is closed")
        val future = CompletableFuture<T>()
        val work = StorageWork(future, task)
        try {
            executor.execute(work)
        } catch (exception: RejectedExecutionException) {
            future.completeExceptionally(exception)
            throw exception
        }
        return future
    }

    override fun close() {
        if (!closed.compareAndSet(false, true)) return
        executor.shutdownNow().forEach { queued ->
            (queued as? StorageWork<*>)?.cancel()
        }
    }

    private class StorageWork<T>(
        private val future: CompletableFuture<T>,
        private val task: () -> T
    ) : Runnable {
        override fun run() {
            if (future.isDone) return
            runCatching(task).fold(future::complete, future::completeExceptionally)
        }

        fun cancel() {
            future.completeExceptionally(RejectedExecutionException("Storage dispatcher was closed"))
        }
    }

    private class StorageThreadFactory : ThreadFactory {
        override fun newThread(runnable: Runnable): Thread = Thread(
            runnable,
            "improved-factions-db-${threadCounter.incrementAndGet()}"
        ).apply { isDaemon = true }
    }

    companion object {
        private val threadCounter = AtomicInteger()

        fun create(databaseType: DatabaseType, mysqlParallelism: Int): StorageDispatcher {
            val parallelism = when (databaseType) {
                DatabaseType.SQLITE -> 1
                DatabaseType.MYSQL -> mysqlParallelism.coerceIn(1, 16)
            }
            return StorageDispatcher(parallelism)
        }
    }
}
