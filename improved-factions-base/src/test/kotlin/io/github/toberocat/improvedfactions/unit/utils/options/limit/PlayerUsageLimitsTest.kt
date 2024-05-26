package io.github.toberocat.improvedfactions.unit.utils.options.limit

import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.utils.options.limit.PlayerUsageLimit
import io.github.toberocat.improvedfactions.utils.options.limit.PlayerUsageLimits
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.*

class PlayerUsageLimitsTest : ImprovedFactionsTest() {

    private val registry = "testRegistry"
    private val playerId = UUID.randomUUID()

    @Test
    fun `getUsageLimit returns existing limit when present`() {
        transaction {
            PlayerUsageLimit.new {
                this.registry = this@PlayerUsageLimitsTest.registry
                this.playerId = this@PlayerUsageLimitsTest.playerId
            }
        }

        val result = PlayerUsageLimits.getUsageLimit(registry, playerId)

        assertNotNull(result)
        assertEquals(registry, result.registry)
        assertEquals(playerId, result.playerId)
    }

    @Test
    fun `getUsageLimit creates new limit when not present`() {
        val result = PlayerUsageLimits.getUsageLimit("newRegistry", UUID.randomUUID())

        assertNotNull(result)
        assertEquals("newRegistry", result.registry)
    }
}