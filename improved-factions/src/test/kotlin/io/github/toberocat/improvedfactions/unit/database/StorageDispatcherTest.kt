package io.github.toberocat.improvedfactions.unit.database

import io.github.toberocat.improvedfactions.database.DatabaseType
import io.github.toberocat.improvedfactions.database.storage.StorageDispatcher
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class StorageDispatcherTest {
    @Test
    fun `sqlite work is serialized on a dedicated database thread`() {
        val dispatcher = StorageDispatcher.create(DatabaseType.SQLITE, mysqlParallelism = 4)
        val callerThread = Thread.currentThread().name
        val active = AtomicInteger()
        val maximumActive = AtomicInteger()
        val release = CountDownLatch(1)

        val futures = (1..8).map {
            dispatcher.submit {
                val nowActive = active.incrementAndGet()
                maximumActive.accumulateAndGet(nowActive, ::maxOf)
                release.await(2, TimeUnit.SECONDS)
                active.decrementAndGet()
                Thread.currentThread().name
            }
        }

        release.countDown()
        val workerNames = futures.map { it.toCompletableFuture().get(2, TimeUnit.SECONDS) }
        dispatcher.close()

        assertTrue(workerNames.all { it.startsWith("improved-factions-db-") })
        assertTrue(workerNames.all { it != callerThread })
        assertTrue(maximumActive.get() == 1)
    }

    @Test
    fun `mariadb work uses configured bounded parallelism`() {
        val dispatcher = StorageDispatcher.create(DatabaseType.MYSQL, mysqlParallelism = 3)
        val release = CountDownLatch(1)
        val started = CountDownLatch(3)
        val active = AtomicInteger()
        val maximumActive = AtomicInteger()

        val futures = (1..12).map {
            dispatcher.submit {
                val nowActive = active.incrementAndGet()
                maximumActive.accumulateAndGet(nowActive, ::maxOf)
                started.countDown()
                release.await(2, TimeUnit.SECONDS)
                active.decrementAndGet()
            }
        }

        assertTrue(started.await(2, TimeUnit.SECONDS))
        release.countDown()
        futures.forEach { it.toCompletableFuture().get(2, TimeUnit.SECONDS) }
        dispatcher.close()

        assertTrue(maximumActive.get() in 2..3)
    }

    @Test
    fun `graceful shutdown completes an already queued write before returning`() {
        val dispatcher = StorageDispatcher.create(DatabaseType.SQLITE, mysqlParallelism = 4)
        val started = CountDownLatch(1)
        val release = CountDownLatch(1)
        val persisted = AtomicBoolean()
        val running = dispatcher.submit {
            started.countDown()
            release.await(2, TimeUnit.SECONDS)
        }
        assertTrue(started.await(2, TimeUnit.SECONDS))
        val queuedWrite = dispatcher.submit { persisted.set(true) }

        Thread {
            Thread.sleep(50)
            release.countDown()
        }.start()

        assertTrue(dispatcher.close(2, TimeUnit.SECONDS))

        assertTrue(persisted.get())
        assertTrue(queuedWrite.toCompletableFuture().isDone)
        assertTrue(running.toCompletableFuture().isDone)
        assertFailsWith<Exception> { dispatcher.submit { Unit } }
    }

    @Test
    fun `graceful shutdown falls back to cancellation after its timeout`() {
        val dispatcher = StorageDispatcher.create(DatabaseType.SQLITE, mysqlParallelism = 4)
        val started = CountDownLatch(1)
        val release = CountDownLatch(1)
        val running = dispatcher.submit {
            started.countDown()
            release.await()
        }
        assertTrue(started.await(2, TimeUnit.SECONDS))
        val queued = dispatcher.submit { "never" }

        assertTrue(!dispatcher.close(10, TimeUnit.MILLISECONDS))

        assertTrue(queued.toCompletableFuture().isCompletedExceptionally)
        assertFailsWith<Exception> { dispatcher.submit { Unit } }
        release.countDown()
        assertTrue(running.toCompletableFuture().isDone)
    }
}
