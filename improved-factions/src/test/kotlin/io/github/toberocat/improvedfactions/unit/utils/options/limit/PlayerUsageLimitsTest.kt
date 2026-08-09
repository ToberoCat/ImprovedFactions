package io.github.toberocat.improvedfactions.unit.utils.options.limit

import io.github.toberocat.improvedfactions.database.storage.ClaimStateCache
import io.github.toberocat.improvedfactions.database.storage.GameStateSnapshot
import io.github.toberocat.improvedfactions.database.storage.UsageLimitKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

@io.github.toberocat.improvedfactions.testing.UnitTest
class PlayerUsageLimitsTest {

    private val registry = "testRegistry"
    private val playerId = UUID.randomUUID()

    @Test
    fun `getUsageLimit returns existing limit when present`() {
        val cache = ClaimStateCache()
        cache.publish(GameStateSnapshot(emptyMap(), emptyMap(), emptyMap(), emptyMap(),
            usageLimits = mapOf(UsageLimitKey(registry, playerId) to 3)))

        assertEquals(3, cache.usage(registry, playerId))
    }

    @Test
    fun `missing usage limit has no mutable entity fallback`() {
        val cache = ClaimStateCache().also {
            it.publish(GameStateSnapshot(emptyMap(), emptyMap(), emptyMap(), emptyMap()))
        }
        assertEquals(null, cache.usage("newRegistry", UUID.randomUUID()))
    }
}
