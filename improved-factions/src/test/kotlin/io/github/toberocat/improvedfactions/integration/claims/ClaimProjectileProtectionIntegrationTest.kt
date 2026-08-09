package io.github.toberocat.improvedfactions.integration.claims

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.claimKey
import org.bukkit.Location
import org.bukkit.entity.Arrow
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.util.Vector
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * The configured `projectiles` protection promises to prevent projectile
 * damage, so it must cancel the damage event rather than only a hit event.
 */
class ClaimProjectileProtectionIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `player fired arrow cannot damage a player in a safezone`() = scenario {
        val attacker = player("archer")
        val target = player("target")
        val testWorld = world("projectile-safezone")
        val safezoneChunk = testWorld.getChunkAt(2, 1)
        val protectedLocation = Location(testWorld, 40.0, 64.0, 24.0)
        attacker.location = protectedLocation.clone().add(-20.0, 0.0, 0.0)
        target.location = protectedLocation
        GameStateCommands.setZone(listOf(safezoneChunk.claimKey()), "safezone").await()
        awaitStorage()

        val arrow = testWorld.spawnArrow(attacker.location, Vector(1, 0, 0), 1F, 0F) as Arrow
        arrow.shooter = attacker
        val damage = EntityDamageByEntityEvent(arrow, target, EntityDamageEvent.DamageCause.PROJECTILE, 4.0)

        server.pluginManager.callEvent(damage)

        assertTrue(damage.isCancelled, "Safezone projectile protection must cancel the actual damage event")
    }

    @Test
    fun `player fired arrow remains allowed in wilderness`() = scenario {
        val attacker = player("archer")
        val target = player("target")
        val testWorld = world("projectile-wilderness")
        val location = Location(testWorld, 40.0, 64.0, 24.0)
        attacker.location = location.clone().add(-20.0, 0.0, 0.0)
        target.location = location

        val arrow = testWorld.spawnArrow(attacker.location, Vector(1, 0, 0), 1F, 0F) as Arrow
        arrow.shooter = attacker
        val damage = EntityDamageByEntityEvent(arrow, target, EntityDamageEvent.DamageCause.PROJECTILE, 4.0)

        server.pluginManager.callEvent(damage)

        assertFalse(damage.isCancelled, "Projectile protection must not cancel wilderness combat")
    }
}
