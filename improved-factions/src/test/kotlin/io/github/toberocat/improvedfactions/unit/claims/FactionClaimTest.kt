package io.github.toberocat.improvedfactions.unit.claims

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.claimKey
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.junit.jupiter.api.Test
import kotlin.test.*

class FactionClaimTest : ImprovedFactionsTest() {
    @Test
    fun `test can claim in allowed world`() {
        val player = createTestPlayer()
        val world = testWorld()
        val chunk = world.getChunkAt(0, 0)
        testFaction(player.uniqueId)
        player.location = chunk.getBlock(8, 8, 8).location

        assertTrue(server.dispatchCommand(player, "f claim"))
        awaitStorage()

        assertNotNull(StorageManager.cache.claim(chunk.claimKey()))
    }

    @Test
    fun `test can't claim in blocked world`() {
        val player = createTestPlayer()
        val world = testWorld()
        val chunk = world.getChunkAt(0, 0)
        testFaction(player.uniqueId)
        player.location = chunk.getBlock(8, 8, 8).location
        BaseModule.config.allowedWorlds = setOf("other-world")

        assertFalse(server.dispatchCommand(player, "f claim"))
        awaitStorage()

        assertNull(StorageManager.cache.claim(chunk.claimKey()))
    }
}
