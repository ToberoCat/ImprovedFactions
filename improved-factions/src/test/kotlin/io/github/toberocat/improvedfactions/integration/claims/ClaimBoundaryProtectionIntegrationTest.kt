package io.github.toberocat.improvedfactions.integration.claims

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.claimKey
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPistonExtendEvent
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Classic Factions semantics are the default: cross-claim pistons and fluids
 * work unless a zone explicitly enables the respective boundary rule.
 */
class ClaimBoundaryProtectionIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `piston movement into a safezone remains allowed by default`() = scenario {
        val testWorld = world("default-piston-boundary")
        val source = testWorld.getChunkAt(0, 0).getBlock(15, 64, 8).apply { type = Material.STONE }
        val targetChunk = testWorld.getChunkAt(1, 0)
        GameStateCommands.setZone(listOf(targetChunk.claimKey()), "safezone").await()
        awaitStorage()

        val event = BlockPistonExtendEvent(source, listOf(source), BlockFace.EAST)
        server.pluginManager.callEvent(event)

        assertFalse(event.isCancelled, "Piston boundary protection must default to disabled")
    }

    @Test
    fun `enabled piston boundary protection blocks movement into a safezone`() = scenario {
        val testWorld = world("protected-piston-boundary")
        val source = testWorld.getChunkAt(0, 0).getBlock(15, 64, 8).apply { type = Material.STONE }
        val targetChunk = testWorld.getChunkAt(1, 0)
        GameStateCommands.setZone(listOf(targetChunk.claimKey()), "safezone").await()
        awaitStorage()
        enableBoundaryProtection("safezone", "piston-boundary")

        val event = BlockPistonExtendEvent(source, listOf(source), BlockFace.EAST)
        server.pluginManager.callEvent(event)

        assertTrue(event.isCancelled, "Enabled piston protection must stop a cross-boundary push into a protected claim")
    }

    @Test
    fun `enabled piston boundary protection leaves raidable faction claims open`() = scenario {
        val owner = player("owner")
        val testWorld = world("raidable-piston-boundary")
        val source = testWorld.getChunkAt(0, 0).getBlock(15, 64, 8).apply { type = Material.STONE }
        val targetChunk = testWorld.getChunkAt(1, 0)
        val faction = faction(owner.uniqueId)
        GameStateCommands.claimAll(listOf(targetChunk.claimKey()), faction.id, accumulatedPower = 0).await()
        awaitStorage()
        enableBoundaryProtection("default", "piston-boundary")

        val event = BlockPistonExtendEvent(source, listOf(source), BlockFace.EAST)
        server.pluginManager.callEvent(event)

        assertFalse(event.isCancelled, "Raidable claims must remain open to piston movement")
    }

    @Test
    fun `fluid flow into a safezone remains allowed by default`() = scenario {
        val testWorld = world("default-fluid-boundary")
        val source = testWorld.getChunkAt(0, 0).getBlock(15, 64, 8).apply { type = Material.WATER }
        val target = testWorld.getChunkAt(1, 0).getBlock(0, 64, 8)
        GameStateCommands.setZone(listOf(target.chunk.claimKey()), "safezone").await()
        awaitStorage()

        val event = BlockFromToEvent(source, target)
        server.pluginManager.callEvent(event)

        assertFalse(event.isCancelled, "Fluid-flow boundary protection must default to disabled")
    }

    @Test
    fun `enabled fluid boundary protection blocks lava and water entering a safezone`() = scenario {
        val testWorld = world("protected-fluid-boundary")
        val target = testWorld.getChunkAt(1, 0).getBlock(0, 64, 8)
        GameStateCommands.setZone(listOf(target.chunk.claimKey()), "safezone").await()
        awaitStorage()
        enableBoundaryProtection("safezone", "fluid-flow-boundary")

        listOf(Material.WATER, Material.LAVA).forEach { liquid ->
            val source = testWorld.getChunkAt(0, 0).getBlock(15, 64, 8).apply { type = liquid }
            val event = BlockFromToEvent(source, target)
            server.pluginManager.callEvent(event)
            assertTrue(event.isCancelled, "Enabled fluid protection must stop $liquid crossing into a safezone")
        }
    }

    @Test
    fun `enabled fluid boundary protection leaves raidable faction claims open`() = scenario {
        val owner = player("owner")
        val testWorld = world("raidable-fluid-boundary")
        val source = testWorld.getChunkAt(0, 0).getBlock(15, 64, 8).apply { type = Material.LAVA }
        val target = testWorld.getChunkAt(1, 0).getBlock(0, 64, 8)
        val faction = faction(owner.uniqueId)
        GameStateCommands.claimAll(listOf(target.chunk.claimKey()), faction.id, accumulatedPower = 0).await()
        awaitStorage()
        enableBoundaryProtection("default", "fluid-flow-boundary")

        val event = BlockFromToEvent(source, target)
        server.pluginManager.callEvent(event)

        assertFalse(event.isCancelled, "Raidable claims must remain open to fluid flow")
    }

    private fun enableBoundaryProtection(zone: String, rule: String) {
        plugin.config.set("zones.$zone.protection.$rule", true)
        ZoneHandler.createZone(plugin, zone, checkNotNull(plugin.config.getConfigurationSection("zones.$zone")))
    }
}
