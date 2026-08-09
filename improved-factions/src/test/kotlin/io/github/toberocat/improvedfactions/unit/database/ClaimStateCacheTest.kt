package io.github.toberocat.improvedfactions.unit.database

import io.github.toberocat.improvedfactions.database.storage.ClaimKey
import io.github.toberocat.improvedfactions.database.storage.ClaimSnapshot
import io.github.toberocat.improvedfactions.database.storage.ClaimStateCache
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.GameStateSnapshot
import io.github.toberocat.improvedfactions.database.storage.UserSnapshot
import io.github.toberocat.improvedfactions.database.storage.RankSnapshot
import io.github.toberocat.improvedfactions.database.storage.InviteSnapshot
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@io.github.toberocat.improvedfactions.testing.UnitTest
class ClaimStateCacheTest {
    @Test
    fun `cache publishes complete immutable snapshots atomically`() {
        val cache = ClaimStateCache()
        val key = ClaimKey("world", 4, -7)
        val owner = UUID.randomUUID()
        val player = UUID.randomUUID()
        val claims = mutableMapOf(key to ClaimSnapshot(key, 11, "default", null, isRaidable = false))
        val factions = mutableMapOf(
            11 to FactionSnapshot(11, "Builders", owner, "invite only", 25, 50, 1)
        )
        val users = mutableMapOf(player to UserSnapshot(player, 11, "Member"))

        assertFalse(cache.isReady())
        assertNull(cache.claim(key))

        cache.publish(GameStateSnapshot(claims, factions, users, mapOf(owner to "Owner")))
        claims.clear()
        factions.clear()
        users.clear()

        assertTrue(cache.isReady())
        assertEquals(11, cache.claim(key)?.factionId)
        assertEquals("Builders", cache.faction(11)?.name)
        assertEquals("Member", cache.user(player)?.rankName)
        assertEquals("Owner", cache.playerName(owner))

        cache.clear()
        assertFalse(cache.isReady())
    }

    @Test
    fun `published nested collections cannot be changed through their source`() {
        val cache = ClaimStateCache()
        val permissions = mutableSetOf("factions.claim")
        val ranks = mutableMapOf(7 to RankSnapshot(7, 11, "Member", 10, permissions))

        cache.publish(
            GameStateSnapshot(
                emptyMap(),
                emptyMap(),
                emptyMap(),
                emptyMap(),
                ranks = ranks
            )
        )
        permissions.clear()
        ranks.clear()

        assertEquals(setOf("factions.claim"), cache.rank(7)?.permissions)
    }

    @Test
    fun `expired invites never participate in gameplay decisions`() {
        val cache = ClaimStateCache()
        val player = UUID.randomUUID()
        cache.publish(
            GameStateSnapshot(
                emptyMap(),
                emptyMap(),
                mapOf(player to UserSnapshot(player, -1, "Guest", id = 4)),
                emptyMap(),
                invites = mapOf(
                    1 to InviteSnapshot(1, 3, 4, 8, 2, System.currentTimeMillis() - 1),
                    2 to InviteSnapshot(2, 3, 4, 9, 2, System.currentTimeMillis() + 60_000)
                )
            )
        )

        assertEquals(listOf(2), cache.invites(player).map { it.id })
    }
}
