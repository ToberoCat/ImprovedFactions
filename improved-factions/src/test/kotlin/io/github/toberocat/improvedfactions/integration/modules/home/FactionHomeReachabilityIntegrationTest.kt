package io.github.toberocat.improvedfactions.integration.modules.home

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.claimKey
import org.awaitility.kotlin.await
import org.bukkit.Location
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Public home contract: a stored home survives territory changes but is only usable in its faction's claim. */
class FactionHomeReachabilityIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `unclaimed home is kept but rejects teleport and notifies every online member`() = scenario {
        val owner = player("home-owner")
        val member = player("home-member")
        val homeWorld = world("home-reachability")
        val faction = faction(owner.uniqueId, member.uniqueId)
        val home = Location(homeWorld, 8.0, 64.0, 8.0)
        val homeKey = home.claimKey()
        val originalLocation = owner.location.clone()
        GameStateCommands.claim(homeKey, faction.id).await()
        GameStateCommands.setHome(faction.id, io.github.toberocat.improvedfactions.database.storage.HomeSnapshot(
            faction.id, homeWorld.name, home.x, home.y, home.z,
        )).await()
        GameStateCommands.unclaim(homeKey).await()
        awaitStorage()

        command("/f home")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .expectMessageContaining("no longer claimed")

        assertTrue(checkNotNull(member.nextMessage()).contains("no longer claimed", ignoreCase = true))
        assertNotNull(StorageManager.cache.home(faction.id), "Unclaiming must not delete the stored home")
        assertEquals(originalLocation, owner.location, "An unreachable home must not teleport the command sender")
    }

    @Test
    fun `reclaimed stored home teleports only after countdown and movement cancels without success`() = scenario {
        val owner = player("home-owner")
        val homeWorld = world("home-reclaim")
        val faction = faction(owner.uniqueId)
        val home = Location(homeWorld, 8.0, 64.0, 8.0)
        val homeKey = home.claimKey()
        GameStateCommands.setHome(faction.id, io.github.toberocat.improvedfactions.database.storage.HomeSnapshot(
            faction.id, homeWorld.name, home.x, home.y, home.z,
        )).await()
        GameStateCommands.claim(homeKey, faction.id).await()
        awaitStorage()

        owner.location = Location(homeWorld, 40.0, 64.0, 40.0)
        command("/f home")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .expectDeclaredResponse("home", "teleportStarted")

        owner.location = Location(homeWorld, 56.0, 64.0, 40.0)
        ticks(1)

        assertEquals(56.0, owner.location.x, "Cancellation must prevent the delayed teleport")
        assertTrue(checkNotNull(owner.nextMessage()).contains("Teleportation cancelled", ignoreCase = true))

        command("/f home")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .expectDeclaredResponse("home", "teleportStarted")

        await.pollInSameThread().atMost(Duration.ofSeconds(6)).pollInterval(Duration.ofMillis(50)).untilAsserted {
            ticks(20)
            assertEquals(home.x, owner.location.x)
            assertEquals(home.y, owner.location.y)
            assertEquals(home.z, owner.location.z)
        }
        assertTrue(checkNotNull(owner.nextMessage()).contains("been teleported", ignoreCase = true))
    }
}
