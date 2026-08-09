package io.github.toberocat.improvedfactions.testing

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FactionsIntegrationTestFixtureTest : FactionsIntegrationTest() {
    @Test
    fun `scenario creates named players and allowed worlds and advances ticks`() {
        scenario {
            assertEquals("ScenarioPlayer", player("ScenarioPlayer").name)
            assertEquals("scenario-world", world("scenario-world").name)
            ticks(2)
        }
    }

    @Test
    fun `faction fixture persists owner and members`() {
        val owner = player("Owner")
        val member = player("Member")

        val created = faction(owner.uniqueId, member.uniqueId)

        assertEquals(created.id, StorageManager.cache.user(owner.uniqueId)?.factionId)
        assertEquals(created.id, StorageManager.cache.user(member.uniqueId)?.factionId)
    }

    @Test
    fun `command helper accepts slash prefixed commands and storage await publishes their result`() {
        val actor = player("Commander")

        assertTrue(execute(actor, "/f create FixtureFaction"))
        awaitStorage()

        assertNotNull(StorageManager.cache.user(actor.uniqueId)?.factionId)
    }
}
