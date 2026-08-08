package io.github.toberocat.improvedfactions.integration.database

import io.github.toberocat.improvedfactions.database.*
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.power.config.PowerManagementConfig
import io.github.toberocat.improvedfactions.modules.relations.RelationType
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.time.Instant
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import kotlin.test.*

class GameStateCommandsIntegrationTest {
    @Test
    fun `commands publish one complete snapshot after each committed workflow`() {
        val file = Files.createTempFile("improved-factions-commands-", ".sqlite").toFile().also { it.delete() }
        val settings = DatabaseSettings(DatabaseType.SQLITE, "jdbc:sqlite:${file.absolutePath}", null, null, 1)
        DatabaseMigrator.migrate(settings.jdbcUrl, "classpath:db/migration/sqlite")
        val dataSource = createDataSource(settings)
        val owner = UUID.randomUUID()
        val otherOwner = UUID.randomUUID()
        val ranks = listOf(
            GameStateCommands.DefaultRankSpec("Member", 1, setOf("send-invites")),
            GameStateCommands.DefaultRankSpec("Owner", 100, setOf("manage-claims"))
        )

        StorageManager.start(settings, dataSource, Logger.getAnonymousLogger()) { it.run() }
        StorageManager.initializeSnapshotRepository(PowerManagementConfig(), "Guest")
            .toCompletableFuture().get(5, TimeUnit.SECONDS)
        try {
            val firstId = GameStateCommands.createFaction(owner, "First", 50, ranks, setOf(
                "send-invites", "manage-claims"
            )).await()
            val secondId = GameStateCommands.createFaction(otherOwner, "Second", 50, ranks, emptySet()).await()
            val first = requireNotNull(StorageManager.cache.faction(firstId))
            val claimKey = ClaimKey("world", 4, -3)

            GameStateCommands.claim(claimKey, firstId).await()
            GameStateCommands.setHome(firstId, HomeSnapshot(firstId, "world", 65.0, 70.0, -47.0)).await()
            GameStateCommands.createInvite(
                owner,
                otherOwner,
                firstId,
                first.defaultRankId,
                Instant.now().plusSeconds(300)
            ).await()
            GameStateCommands.createRelation(firstId, secondId, RelationType.ENEMY.ordinal).await()
            GameStateCommands.updateUsage("wilderness", owner, 3).await()
            GameStateCommands.createRank(firstId, "Builder", 5, setOf("manage-claims")).await()

            val snapshot = requireNotNull(StorageManager.cache.snapshot())
            assertEquals(firstId, snapshot.claims[claimKey]?.factionId)
            assertEquals("default", snapshot.claims[claimKey]?.zoneType)
            assertEquals("world", snapshot.homes[firstId]?.world)
            assertTrue(StorageManager.cache.invites(otherOwner).any { it.factionId == firstId })
            assertEquals(setOf(secondId), StorageManager.cache.relations(firstId, RelationType.ENEMY.name))
            assertEquals(3, StorageManager.cache.usage("wilderness", owner))
            assertTrue(StorageManager.cache.ranks(firstId).any { it.name == "Builder" })
        } finally {
            StorageManager.close()
        }
    }

    private fun <T> java.util.concurrent.CompletionStage<T>.await(): T =
        toCompletableFuture().get(5, TimeUnit.SECONDS)
}
