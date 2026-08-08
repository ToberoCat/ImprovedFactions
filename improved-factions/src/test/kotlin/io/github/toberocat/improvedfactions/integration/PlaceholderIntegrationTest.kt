package io.github.toberocat.improvedfactions.integration

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.integrations.papi.PlaceholderIntegration
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PlaceholderIntegrationTest : ImprovedFactionsTest() {
    @Test
    fun `members total placeholder counts all faction members`() {
        val owner = createTestPlayer("Owner")
        val member = createTestPlayer("Member")
        testFaction(owner.uniqueId, member.uniqueId)

        assertEquals("2", PlaceholderIntegration.parsePlaceholder(owner, "members_total"))
    }

    @Test
    fun `members online placeholder only counts online faction members`() {
        val owner = createTestPlayer("Owner")
        val member = createTestPlayer("Member")
        testFaction(owner.uniqueId, member.uniqueId)
        member.disconnect()

        assertEquals("1", PlaceholderIntegration.parsePlaceholder(owner, "members_online"))
    }
}
