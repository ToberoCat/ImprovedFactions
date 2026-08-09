package io.github.toberocat.improvedfactions.integration.claims

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.claimKey
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityExplodeEvent
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

/**
 * Covers the Bukkit event boundary for the protection promised by the default
 * safezone configuration.  The source is deliberately outside the protected
 * chunk: this is the TNT-cannon case from #359.
 */
class ClaimExplosionProtectionIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `TNT launched from wilderness cannot destroy blocks in a safezone`() = scenario {
        val testWorld = world("explosion-protection")
        val protectedChunk = testWorld.getChunkAt(3, 0)
        val protectedBlock = protectedChunk.getBlock(8, 64, 8)
        GameStateCommands.setZone(listOf(protectedChunk.claimKey()), "safezone").await()
        awaitStorage()

        val source = testWorld.spawnEntity(Location(testWorld, 8.0, 64.0, 8.0), EntityType.TNT)
        val explosion = EntityExplodeEvent(
            source,
            source.location,
            mutableListOf(protectedBlock),
            4.0F,
            org.bukkit.ExplosionResult.DESTROY,
        )

        server.pluginManager.callEvent(explosion)

        assertTrue(explosion.blockList().isEmpty(), "Protected blocks must be removed from TNT's destruction list")
    }
}
