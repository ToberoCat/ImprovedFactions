package io.github.toberocat.improvedfactions.unit.modules.home

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import org.junit.jupiter.api.Test
import kotlin.test.*

class HomeModuleTest : ImprovedFactionsTest() {
    @Test
    fun `test home creation in claim`() {
        val player = createTestPlayer()
        val world = testWorld()
        val chunk = world.getChunkAt(0, 0)
        val faction = testFaction(player.uniqueId)
        player.location = chunk.getBlock(8, 8, 8).location

        assertTrue(server.dispatchCommand(player, "f claim"))
        awaitStorage()
        assertTrue(server.dispatchCommand(player, "f sethome"))
        awaitStorage()

        assertNotNull(StorageManager.cache.home(faction.id))
    }

    @Test
    fun `no homes allowed outside of claimed regions`() {
        val player = createTestPlayer()
        val world = testWorld()
        val faction = testFaction(player.uniqueId)
        player.location = world.getBlockAt(0, 64, 0).location

        assertTrue(server.dispatchCommand(player, "f sethome"))
        awaitStorage()

        assertNull(StorageManager.cache.home(faction.id))
    }
}
