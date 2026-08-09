package io.github.toberocat.improvedfactions.unit.database

import io.github.toberocat.improvedfactions.database.DatabaseType
import io.github.toberocat.improvedfactions.database.storage.ClaimStateCache
import io.github.toberocat.improvedfactions.database.storage.ClaimRepository
import io.github.toberocat.improvedfactions.database.storage.GameStateSnapshot
import io.github.toberocat.improvedfactions.database.storage.StorageDispatcher
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@io.github.toberocat.improvedfactions.testing.UnitTest
class GameStateRepositoryTest {
    @Test
    fun `cache changes only after successful asynchronous load`() {
        val cache = ClaimStateCache()
        val dispatcher = StorageDispatcher.create(DatabaseType.SQLITE, mysqlParallelism = 4)
        val started = CountDownLatch(1)
        val release = CountDownLatch(1)
        val snapshot = GameStateSnapshot(emptyMap(), emptyMap(), emptyMap(), emptyMap())
        val repository = ClaimRepository(dispatcher, cache) {
            started.countDown()
            release.await(2, TimeUnit.SECONDS)
            snapshot
        }

        val refresh = repository.refresh()
        assertTrue(started.await(2, TimeUnit.SECONDS))
        assertFalse(cache.isReady())

        release.countDown()
        refresh.toCompletableFuture().get(2, TimeUnit.SECONDS)

        assertTrue(cache.isReady())
        assertEquals(snapshot, cache.snapshot())
        dispatcher.close()
    }

    @Test
    fun `failed load never replaces the last committed cache snapshot`() {
        val initial = GameStateSnapshot(emptyMap(), emptyMap(), emptyMap(), mapOf())
        val cache = ClaimStateCache().also { it.publish(initial) }
        val dispatcher = StorageDispatcher.create(DatabaseType.SQLITE, mysqlParallelism = 4)
        val repository = ClaimRepository(dispatcher, cache) { error("database failure") }

        runCatching { repository.refresh().toCompletableFuture().get(2, TimeUnit.SECONDS) }

        assertEquals(initial, cache.snapshot())
        dispatcher.close()
    }

    @Test
    fun `refresh keeps the last committed snapshot visible while reload is in flight`() {
        val initial = GameStateSnapshot(emptyMap(), emptyMap(), emptyMap(), mapOf())
        val replacement = initial.copy(playerNames = mapOf(java.util.UUID.randomUUID() to "Player"))
        val cache = ClaimStateCache().also { it.publish(initial) }
        val dispatcher = StorageDispatcher.create(DatabaseType.SQLITE, mysqlParallelism = 4)
        val started = CountDownLatch(1)
        val release = CountDownLatch(1)
        val repository = ClaimRepository(dispatcher, cache) {
            started.countDown()
            release.await(2, TimeUnit.SECONDS)
            replacement
        }

        val refresh = repository.refresh()
        assertTrue(started.await(2, TimeUnit.SECONDS))
        assertEquals(initial, cache.snapshot())

        release.countDown()
        refresh.toCompletableFuture().get(2, TimeUnit.SECONDS)
        assertEquals(replacement, cache.snapshot())
        dispatcher.close()
    }

    @Test
    fun `older parallel refresh cannot replace a newer published snapshot`() {
        val initial = GameStateSnapshot(emptyMap(), emptyMap(), emptyMap(), emptyMap())
        val older = initial.copy(playerNames = mapOf(java.util.UUID(0, 1) to "older"))
        val newer = initial.copy(playerNames = mapOf(java.util.UUID(0, 2) to "newer"))
        val cache = ClaimStateCache().also { it.publish(initial) }
        val dispatcher = StorageDispatcher.create(DatabaseType.MYSQL, mysqlParallelism = 2)
        val call = AtomicInteger()
        val firstStarted = CountDownLatch(1)
        val releaseFirst = CountDownLatch(1)
        val repository = ClaimRepository(dispatcher, cache) {
            if (call.incrementAndGet() == 1) {
                firstStarted.countDown()
                releaseFirst.await(2, TimeUnit.SECONDS)
                older
            } else newer
        }

        val first = repository.refresh()
        assertTrue(firstStarted.await(2, TimeUnit.SECONDS))
        repository.refresh().toCompletableFuture().get(2, TimeUnit.SECONDS)
        assertEquals(newer, cache.snapshot())

        releaseFirst.countDown()
        first.toCompletableFuture().get(2, TimeUnit.SECONDS)
        assertEquals(newer, cache.snapshot())
        dispatcher.close()
    }
}
