package io.github.toberocat.improvedfactions.integration.database

import io.github.toberocat.improvedfactions.database.DatabaseMigrator
import io.github.toberocat.improvedfactions.database.DatabaseSettings
import io.github.toberocat.improvedfactions.database.DatabaseType
import io.github.toberocat.improvedfactions.database.createDataSource
import io.github.toberocat.improvedfactions.database.storage.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import java.nio.ByteBuffer
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

@EnabledIfEnvironmentVariable(named = "MARIADB_TEST_URL", matches = ".+")
class JdbcStorageRepositoryMariaDbTest {
    @Test
    fun `mariadb write commits and reloads the cache before completion`() {
        val url = requireNotNull(System.getenv("MARIADB_TEST_URL"))
        val user = System.getenv("MARIADB_TEST_USER") ?: "root"
        val password = System.getenv("MARIADB_TEST_PASSWORD") ?: ""
        DatabaseMigrator.migrate(url, "classpath:db/migration/mysql", user, password)
        val registry = "repository-${UUID.randomUUID()}".take(30)
        val playerId = UUID.randomUUID()
        val dataSource = createDataSource(DatabaseSettings(DatabaseType.MYSQL, url, user, password, 3))
        val dispatcher = StorageDispatcher.create(DatabaseType.MYSQL, 2)
        val cache = ClaimStateCache()
        val loader = JdbcGameStateLoader(dataSource, RaidSnapshotConfig(false, 1.0, "Guest"))
        val snapshots = ClaimRepository(dispatcher, cache, loader::load)
        val repository = JdbcStorageRepository(dataSource, dispatcher, snapshots)

        try {
            repository.write { connection ->
                connection.prepareStatement(
                    "INSERT INTO player_usage_limits (registry, player_id, used) VALUES (?, ?, ?)"
                ).use {
                    it.setString(1, registry)
                    it.setBytes(2, playerId.bytes())
                    it.setInt(3, 7)
                    it.executeUpdate()
                }
            }.toCompletableFuture().get(5, TimeUnit.SECONDS)

            assertEquals(7, cache.usage(registry, playerId))
        } finally {
            dispatcher.close()
            dataSource.connection.use { connection ->
                connection.prepareStatement("DELETE FROM player_usage_limits WHERE registry = ?").use {
                    it.setString(1, registry)
                    it.executeUpdate()
                }
                connection.commit()
            }
            dataSource.close()
        }
    }

    private fun UUID.bytes(): ByteArray = ByteBuffer.allocate(16)
        .putLong(mostSignificantBits)
        .putLong(leastSignificantBits)
        .array()
}
