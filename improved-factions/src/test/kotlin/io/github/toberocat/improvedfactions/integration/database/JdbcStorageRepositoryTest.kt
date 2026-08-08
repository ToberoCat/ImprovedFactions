package io.github.toberocat.improvedfactions.integration.database

import io.github.toberocat.improvedfactions.database.DatabaseMigrator
import io.github.toberocat.improvedfactions.database.DatabaseSettings
import io.github.toberocat.improvedfactions.database.DatabaseType
import io.github.toberocat.improvedfactions.database.createDataSource
import io.github.toberocat.improvedfactions.database.storage.ClaimRepository
import io.github.toberocat.improvedfactions.database.storage.ClaimStateCache
import io.github.toberocat.improvedfactions.database.storage.GameStateSnapshot
import io.github.toberocat.improvedfactions.database.storage.JdbcStorageRepository
import io.github.toberocat.improvedfactions.database.storage.StorageDispatcher
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JdbcStorageRepositoryTest {
    @Test
    fun `write commits before the refreshed snapshot becomes visible`() {
        withRepository { repository, cache ->
            repository.write { connection ->
                connection.prepareStatement("INSERT INTO known_offline_players (id, name) VALUES (randomblob(16), 'Ada')")
                    .use { it.executeUpdate() }
            }.toCompletableFuture().get(3, TimeUnit.SECONDS)

            assertEquals(mapOf(java.util.UUID(0, 0) to "1"), cache.snapshot()?.playerNames)
        }
    }

    @Test
    fun `failed write rolls back and does not publish a snapshot`() {
        withRepository { repository, cache ->
            val before = cache.snapshot()

            assertFailsWith<Exception> {
                repository.write<Unit> { connection ->
                    connection.createStatement().use { it.executeUpdate("INSERT INTO missing_table VALUES (1)") }
                }.toCompletableFuture().get(3, TimeUnit.SECONDS)
            }

            assertEquals(before, cache.snapshot())
        }
    }

    private fun withRepository(block: (JdbcStorageRepository, ClaimStateCache) -> Unit) {
        val file = Files.createTempFile("improved-factions-writes-", ".sqlite").toFile().also { it.delete() }
        val url = "jdbc:sqlite:${file.absolutePath}"
        DatabaseMigrator.migrate(url, "classpath:db/migration/sqlite")
        val dataSource = createDataSource(DatabaseSettings(DatabaseType.SQLITE, url, null, null, 1))
        val dispatcher = StorageDispatcher.create(DatabaseType.SQLITE, 1)
        val cache = ClaimStateCache().also {
            it.publish(GameStateSnapshot(emptyMap(), emptyMap(), emptyMap(), emptyMap()))
        }
        val snapshots = ClaimRepository(dispatcher, cache) {
            val count = dataSource.connection.use { connection ->
                connection.createStatement().use { statement ->
                    statement.executeQuery("SELECT COUNT(*) FROM known_offline_players").use { result ->
                        result.next()
                        result.getInt(1)
                    }
                }
            }
            GameStateSnapshot(emptyMap(), emptyMap(), emptyMap(), mapOf(java.util.UUID(0, 0) to count.toString()))
        }
        try {
            block(JdbcStorageRepository(dataSource, dispatcher, snapshots), cache)
        } finally {
            dispatcher.close()
            dataSource.close()
        }
    }
}
