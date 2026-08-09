package io.github.toberocat.improvedfactions.integration.claims

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.claimKey
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.Action
import org.bukkit.block.BlockFace
import org.bukkit.event.player.PlayerInteractEvent
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Exercises the registered Bukkit protection listeners, not listener internals. */
class ClaimBlockProtectionIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `visitor cannot break a block in an ordinary faction claim`() = scenario {
        val owner = player("owner")
        val visitor = player("visitor")
        val testWorld = world("faction-block-protection")
        val claimedChunk = testWorld.getChunkAt(2, 1)
        val block = claimedChunk.getBlock(8, 64, 8).apply { type = Material.STONE }
        val faction = faction(owner.uniqueId)
        GameStateCommands.claim(claimedChunk.claimKey(), faction.id).await()
        awaitStorage()

        val breakEvent = BlockBreakEvent(block, visitor)
        server.pluginManager.callEvent(breakEvent)

        assertTrue(breakEvent.isCancelled, "A non-member must not break blocks in a protected faction claim")
    }

    @Test
    fun `faction member can break a block in own claim`() = scenario {
        val owner = player("owner")
        val testWorld = world("member-block-protection")
        val claimedChunk = testWorld.getChunkAt(2, 1)
        val block = claimedChunk.getBlock(8, 64, 8).apply { type = Material.STONE }
        val faction = faction(owner.uniqueId)
        GameStateCommands.claim(claimedChunk.claimKey(), faction.id).await()
        awaitStorage()

        val breakEvent = BlockBreakEvent(block, owner)
        server.pluginManager.callEvent(breakEvent)

        assertFalse(breakEvent.isCancelled, "A faction member must retain build access to their own claim")
    }

    @Test
    fun `visitor cannot use an interactable block in a safezone`() = scenario {
        val visitor = player("visitor")
        val testWorld = world("safezone-interaction-protection")
        val safezoneChunk = testWorld.getChunkAt(2, 1)
        val chest = safezoneChunk.getBlock(8, 64, 8).apply { type = Material.CHEST }
        GameStateCommands.setZone(listOf(safezoneChunk.claimKey()), "safezone").await()
        awaitStorage()

        val interact = PlayerInteractEvent(visitor, Action.RIGHT_CLICK_BLOCK, null, chest, BlockFace.UP)
        server.pluginManager.callEvent(interact)

        assertTrue(interact.isCancelled, "Safezone protection must cancel interaction with protected containers")
    }
}
