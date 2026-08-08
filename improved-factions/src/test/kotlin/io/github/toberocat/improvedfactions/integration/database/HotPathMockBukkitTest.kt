package io.github.toberocat.improvedfactions.integration.database

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.database.storage.ClaimKey
import io.github.toberocat.improvedfactions.database.storage.ClaimSnapshot
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.GameStateSnapshot
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.UserSnapshot
import io.github.toberocat.improvedfactions.listeners.claim.ClaimBlockBreakListener
import io.github.toberocat.improvedfactions.integrations.papi.PlaceholderIntegration
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.event.block.BlockBreakEvent
import org.junit.jupiter.api.Test
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HotPathMockBukkitTest : ImprovedFactionsTest() {
    @Test
    fun `protection fails closed while startup snapshot is unavailable`() {
        val world = testWorld("bootstrap-world")
        val player = createTestPlayer()
        StorageManager.close()
        val event = BlockBreakEvent(world.getBlockAt(1, 64, 1), player)

        ClaimBlockBreakListener("default", sendMessage = false).placeEvent(event)

        assertTrue(event.isCancelled)
    }

    @Test
    fun `protection decision is completed synchronously from the committed cache`() {
        val world = testWorld("cache-world")
        val player = createTestPlayer()
        val owner = UUID.randomUUID()
        val claimKey = ClaimKey(world.name, 0, 0)
        StorageManager.close()
        StorageManager.cache.publish(
            GameStateSnapshot(
                mapOf(claimKey to ClaimSnapshot(claimKey, 1, "default", null, isRaidable = false)),
                mapOf(1 to FactionSnapshot(1, "Owners", owner, "invite only", 50, 50, 1)),
                mapOf(player.uniqueId to UserSnapshot(player.uniqueId, 2, "Member")),
                emptyMap()
            )
        )
        val event = BlockBreakEvent(world.getBlockAt(1, 64, 1), player)

        ClaimBlockBreakListener("default", sendMessage = false).placeEvent(event)

        assertTrue(event.isCancelled)
    }

    @Test
    fun `async placeholder caller never touches bukkit state or waits for main thread`() {
        val player = createTestPlayer()

        val result = CompletableFuture.supplyAsync {
            PlaceholderIntegration.parsePlaceholder(player, "name")
        }.get(2, TimeUnit.SECONDS)

        assertEquals(BaseModule.config.defaultPlaceholders["name"], result)
    }
}
