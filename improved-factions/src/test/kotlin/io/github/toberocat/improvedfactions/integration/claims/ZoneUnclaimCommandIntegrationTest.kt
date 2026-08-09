package io.github.toberocat.improvedfactions.integration.claims

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.claimKey
import org.bukkit.Location
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/** Regression coverage for #327. */
class ZoneUnclaimCommandIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `admin zone unclaim removes a wilderness zone instead of creating a default wilderness claim`() = scenario {
        val admin = player("zone-admin")
        val testWorld = world("zone-unclaim-wilderness")
        admin.location = Location(testWorld, 8.0, 64.0, 8.0)
        val key = admin.location.chunk.claimKey()
        GameStateCommands.setZone(listOf(key), "safezone").await()
        awaitStorage()

        command("/f admin zone unclaim 0")
            .asPlayer(admin)
            .run()
            .expectHandled()
            .awaitStorage()

        assertNull(StorageManager.cache.claim(key), "Unclaiming a zone must restore wilderness, not persist a phantom claim")
    }

    @Test
    fun `admin zone unclaim restores an underlying faction claim`() = scenario {
        val admin = player("zone-admin-owner")
        val testWorld = world("zone-unclaim-faction")
        admin.location = Location(testWorld, 8.0, 64.0, 8.0)
        val key = admin.location.chunk.claimKey()
        val faction = faction(admin.uniqueId)
        GameStateCommands.claim(key, faction.id).await()
        GameStateCommands.setZone(listOf(key), "safezone").await()
        awaitStorage()

        command("/f admin zone unclaim 0")
            .asPlayer(admin)
            .run()
            .expectHandled()
            .awaitStorage()

        val restoredClaim = assertNotNull(StorageManager.cache.claim(key))
        assertEquals(faction.id, restoredClaim.factionId)
        assertEquals("default", restoredClaim.zoneType)
    }
}
