package io.github.toberocat.improvedfactions.integration.modules.power

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.ClaimKey
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.Location
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Siege eligibility is a gameplay promise: an enemy claim must already be raidable because
 * its owner lacks enough power.  Attacker power is intentionally irrelevant.
 */
class SiegeCommandTest : FactionsIntegrationTest() {
    @Test
    fun `siege rejects an enemy claim that is still protected by faction power`() {
        val attacker = player("attacker")
        val defender = player("defender")
        faction(attacker.uniqueId)
        val targetFactionId = createDefenderFaction(defender)
        val testWorld = world("protected-siege")
        val targetKey = prepareTargetClaims(testWorld.name, targetFactionId, accumulatedPower = 50)
        attacker.location = Location(testWorld, 8.0, 64.0, 8.0)
        awaitStorage()

        assertTrue(!requireNotNull(StorageManager.cache.claim(targetKey)).isRaidable)

        command("/f siege")
            .asPlayer(attacker)
            .run()
            .expectHandled()
            .expectDeclaredResponse("siege", "notRaidable")
        ticks(2)
        awaitStorage()

        assertEquals(targetFactionId, requireNotNull(StorageManager.cache.claim(targetKey)).factionId)
    }

    @Test
    fun `siege takes over an already raidable enemy claim regardless of attacker power`() {
        val attacker = player("attacker")
        val defender = player("defender")
        val attackerFaction = faction(attacker.uniqueId)
        GameStateCommands.setPower(attackerFaction.id, accumulated = -attackerFaction.maxPower).await()
        val targetFactionId = createDefenderFaction(defender)
        val testWorld = world("raidable-siege")
        val targetKey = prepareTargetClaims(testWorld.name, targetFactionId, accumulatedPower = -50)
        attacker.location = Location(testWorld, 8.0, 64.0, 8.0)
        awaitStorage()
        assertTrue(requireNotNull(StorageManager.cache.claim(targetKey)).isRaidable)

        plugin.moduleManager.getModule<io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule>("power-raids")
            .config.siegeBreachProgress = 100.0

        command("/f siege")
            .asPlayer(attacker)
            .run()
            .expectHandled()
        ticks(1)
        awaitStorage()

        assertEquals(
            noFactionId,
            requireNotNull(StorageManager.cache.claim(targetKey)).factionId,
            "A successful siege must release the raidable target to wilderness",
        )
    }

    @Test
    fun `a single underpowered claim is raidable and can be sieged`() {
        val attacker = player("single-claim-attacker")
        val defender = player("single-claim-defender")
        faction(attacker.uniqueId)
        val targetFactionId = createDefenderFaction(defender)
        val testWorld = world("single-raidable-siege")
        val targetKey = ClaimKey(testWorld.name, 0, 0)
        GameStateCommands.claimAll(listOf(targetKey), targetFactionId, accumulatedPower = -50).await()
        awaitStorage()

        assertTrue(requireNotNull(StorageManager.cache.claim(targetKey)).isRaidable)

        attacker.location = Location(testWorld, 8.0, 64.0, 8.0)
        command("/f siege")
            .asPlayer(attacker)
            .run()
            .expectHandled()
    }

    private fun createDefenderFaction(defender: org.bukkit.entity.Player): Int = GameStateCommands.createFaction(
        defender.uniqueId,
        "Defenders",
        50,
        listOf(
            GameStateCommands.DefaultRankSpec("Member", 1, emptySet()),
            GameStateCommands.DefaultRankSpec("Owner", 1000, Permissions.knownPermissions.keys),
        ),
        Permissions.knownPermissions.keys,
    ).await()

    private fun prepareTargetClaims(world: String, factionId: Int, accumulatedPower: Int): ClaimKey {
        val target = ClaimKey(world, 0, 0)
        GameStateCommands.claimAll(
            listOf(target, ClaimKey(world, 1, 0)),
            factionId,
            accumulatedPower,
        ).await()
        return target
    }
}
