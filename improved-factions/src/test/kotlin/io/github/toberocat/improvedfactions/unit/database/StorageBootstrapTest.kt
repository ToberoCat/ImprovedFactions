package io.github.toberocat.improvedfactions.unit.database

import io.github.toberocat.improvedfactions.database.DatabaseType
import io.github.toberocat.improvedfactions.database.storage.ClaimRepository
import io.github.toberocat.improvedfactions.database.storage.ClaimStateCache
import io.github.toberocat.improvedfactions.database.storage.GameStateSnapshot
import io.github.toberocat.improvedfactions.database.storage.StorageBootstrap
import io.github.toberocat.improvedfactions.database.storage.StorageDispatcher
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class StorageBootstrapTest {
    @Test
    fun `bootstrap returns immediately and loads on the storage worker`() {
        val cache = ClaimStateCache()
        val dispatcher = StorageDispatcher.create(DatabaseType.SQLITE, mysqlParallelism = 4)
        val expected = GameStateSnapshot(emptyMap(), emptyMap(), emptyMap(), mapOf())
        val started = CountDownLatch(1)
        val release = CountDownLatch(1)
        val loaderThread = AtomicReference<String>()
        val repository = ClaimRepository(dispatcher, cache) {
            loaderThread.set(Thread.currentThread().name)
            started.countDown()
            release.await(2, TimeUnit.SECONDS)
            expected
        }

        val bootstrap = StorageBootstrap(repository).loadAsync()

        assertTrue(started.await(2, TimeUnit.SECONDS))
        assertFalse(bootstrap.toCompletableFuture().isDone)
        assertFalse(cache.isReady())
        assertNotEquals(Thread.currentThread().name, loaderThread.get())
        assertTrue(loaderThread.get().startsWith("improved-factions-db-"))

        release.countDown()
        assertEquals(expected, bootstrap.toCompletableFuture().get(2, TimeUnit.SECONDS))
        assertEquals(expected, cache.snapshot())
        dispatcher.close()
    }

    @Test
    fun `failed async bootstrap leaves the safe not-ready state`() {
        val cache = ClaimStateCache()
        val dispatcher = StorageDispatcher.create(DatabaseType.SQLITE, mysqlParallelism = 4)
        val repository = ClaimRepository(dispatcher, cache) { error("broken storage") }

        val bootstrap = StorageBootstrap(repository).loadAsync().toCompletableFuture()

        runCatching { bootstrap.get(2, TimeUnit.SECONDS) }
        assertTrue(bootstrap.isCompletedExceptionally)
        assertFalse(cache.isReady())
        dispatcher.close()
    }
}
