package io.github.toberocat.improvedfactions.integration.claims

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.claimKey
import io.github.toberocat.improvedfactions.testing.IntegrationTest
import org.bukkit.Location
import org.bukkit.entity.Arrow
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.junit.jupiter.api.Test
import org.bukkit.util.Vector
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Regression coverage for #337. */
@IntegrationTest
class InFactionProjectileProtectionIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `default-zone faction claims protect teammates from melee and projectile damage`() {
        scenario {
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

            assertFriendlyFireIsCancelled(attacker, teammate, claimedLocation, Attack.MELEE)
            assertFriendlyFireIsCancelled(attacker, teammate, claimedLocation, Attack.PROJECTILE)
        }
    }

    @Test
    fun `arrow damage between faction teammates is cancelled in wilderness by the default zone`() {
        scenario {
            val attacker = player("archer")
            val teammate = player("teammate")
            val testWorld = world("in-faction-projectile-wilderness")
            val wildernessLocation = Location(testWorld, 40.0, 64.0, 24.0)
            attacker.location = wildernessLocation
            teammate.location = wildernessLocation
            faction(attacker.uniqueId, teammate.uniqueId)
            awaitStorage()

            assertFriendlyFireIsCancelled(attacker, teammate, wildernessLocation, Attack.MELEE)
            assertFriendlyFireIsCancelled(attacker, teammate, wildernessLocation, Attack.PROJECTILE)
        }
    }

    @Test
    fun `each administrative zone applies its friendly-fire setting to melee and projectile damage`() {
        scenario {
            val attacker = player("attacker")
            val teammate = player("teammate")
            val testWorld = world("in-faction-projectile-administrative-zones")
            faction(attacker.uniqueId, teammate.uniqueId)
            awaitStorage()

            mapOf("safezone" to true, "warzone" to true, "unmanaged" to false).entries.forEachIndexed { index, (zone, protected) ->
                val location = Location(testWorld, index * 32.0 + 8, 64.0, 8.0)
                attacker.location = location
                teammate.location = location
                GameStateCommands.setZone(listOf(location.chunk.claimKey()), zone).await()
                awaitStorage()

                Attack.entries.forEach { attack ->
                    val damage = friendlyFireEvent(attacker, teammate, location, attack)
                    server.pluginManager.callEvent(damage)

                    assertEquals(protected, damage.isCancelled, "$zone must apply its in-faction-pvp setting to $attack")
                }
            }
        }
    }

    private fun assertFriendlyFireIsCancelled(attacker: org.bukkit.entity.Player, teammate: org.bukkit.entity.Player, location: Location, attack: Attack) {
        val damage = friendlyFireEvent(attacker, teammate, location, attack)
        server.pluginManager.callEvent(damage)
        assertTrue(damage.isCancelled, "The default zone must protect faction teammates from $attack")
    }

    private fun friendlyFireEvent(attacker: org.bukkit.entity.Player, teammate: org.bukkit.entity.Player, location: Location, attack: Attack): EntityDamageByEntityEvent =
        when (attack) {
            Attack.MELEE -> EntityDamageByEntityEvent(attacker, teammate, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 4.0)
            Attack.PROJECTILE -> {
                val arrow = location.world.spawnArrow(location, Vector(1, 0, 0), 1F, 0F) as Arrow
                arrow.shooter = attacker
                EntityDamageByEntityEvent(arrow, teammate, EntityDamageEvent.DamageCause.PROJECTILE, 4.0)
            }
        }

    private enum class Attack { MELEE, PROJECTILE }
}
