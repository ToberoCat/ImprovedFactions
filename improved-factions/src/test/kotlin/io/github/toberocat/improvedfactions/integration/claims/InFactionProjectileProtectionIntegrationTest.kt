package io.github.toberocat.improvedfactions.integration.claims

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.claimKey
import org.bukkit.Location
import org.bukkit.entity.Arrow
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.junit.jupiter.api.Test
import org.bukkit.util.Vector
import kotlin.test.assertTrue

/** Regression coverage for #337. */
class InFactionProjectileProtectionIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `arrow damage between faction teammates is cancelled inside their claim`() = scenario {
        val attacker = player("archer")
        val teammate = player("teammate")
        val testWorld = world("in-faction-projectile")
        val claimChunk = testWorld.getChunkAt(2, 1)
        val claimedLocation = Location(testWorld, 2 * 16.0 + 8, 64.0, 1 * 16.0 + 8)
        attacker.location = claimedLocation
        teammate.location = claimedLocation
        val faction = faction(attacker.uniqueId, teammate.uniqueId)
        GameStateCommands.claim(claimChunk.claimKey(), faction.id).await()
        awaitStorage()

        val arrow = testWorld.spawnArrow(claimedLocation, Vector(1, 0, 0), 1F, 0F) as Arrow
        arrow.shooter = attacker
        val damage = EntityDamageByEntityEvent(arrow, teammate, EntityDamageEvent.DamageCause.PROJECTILE, 4.0)

        server.pluginManager.callEvent(damage)

        assertTrue(damage.isCancelled, "In-faction PvP protection must include player-fired projectiles")
    }
}
