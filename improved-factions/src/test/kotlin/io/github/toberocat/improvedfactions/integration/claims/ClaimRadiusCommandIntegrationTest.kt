package io.github.toberocat.improvedfactions.integration.claims

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.ClaimKey
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import org.bukkit.Location
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ClaimRadiusCommandIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `claim radius claims every chunk in the requested square around the player's chunk`() = scenario {
        val owner = player("radius-owner")
        val testWorld = world("radius-command")
        owner.location = Location(testWorld, 10 * 16.0 + 8, 64.0, -4 * 16.0 + 8)
        val faction = faction(owner.uniqueId)

        command("/f claim 1")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .awaitStorage()

        val claimedKeys = (-1..1).flatMap { x ->
            (-1..1).map { z -> ClaimKey(testWorld.name, 10 + x, -4 + z) }
        }
        claimedKeys.forEach { key ->
            assertEquals(faction.id, assertNotNull(StorageManager.cache.claim(key)).factionId, "Expected $key to be claimed")
        }
        assertEquals(9, StorageManager.cache.faction(faction.id)?.claimCount)
    }

    @Test
    fun `underpowered radius claim starts at the player chunk and remains contiguous`() = scenario {
        val owner = player("underpowered-radius-owner")
        val testWorld = world("underpowered-radius-command")
        owner.location = Location(testWorld, 8.0, 64.0, 8.0)
        val faction = faction(owner.uniqueId)
        GameStateCommands.setPower(faction.id, accumulated = 11).await()
        awaitStorage()

        command("/f claim 1")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .awaitStorage()

        val expectedClaims = setOf(
            ClaimKey(testWorld.name, 0, 0),
            ClaimKey(testWorld.name, 1, 0),
        )
        expectedClaims.forEach { key ->
            assertEquals(faction.id, assertNotNull(StorageManager.cache.claim(key)).factionId)
        }
        assertEquals(2, StorageManager.cache.faction(faction.id)?.claimCount)
        assertEquals(null, StorageManager.cache.claim(ClaimKey(testWorld.name, -1, -1)))
    }

    @Test
    fun `claim reports localized error when it cannot afford the player chunk`() = scenario {
        val owner = player("no-power-owner")
        val testWorld = world("no-power-command")
        owner.location = Location(testWorld, 8.0, 64.0, 8.0)
        val faction = faction(owner.uniqueId)
        GameStateCommands.setPower(faction.id, accumulated = 0).await()
        awaitStorage()

        command("/f claim")
            .asPlayer(owner)
            .run()
            .expectHandled(false)
            .expectLocalizedResponse(
                "base.exceptions.not-enough-power-for-claim",
                mapOf("x" to "0", "z" to "0"),
            )
    }
}
