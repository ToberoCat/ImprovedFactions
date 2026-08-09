package io.github.toberocat.improvedfactions.integration.modules.home

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.claimKey
import io.github.toberocat.improvedfactions.testing.IntegrationTest
import org.awaitility.kotlin.await
import org.bukkit.Location
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Public home contract: a stored home survives territory changes but is only usable in its faction's claim. */
@IntegrationTest
class FactionHomeReachabilityIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `unclaiming the home chunk keeps it unreachable and warns every online member`() {
        scenario {
            val owner = player("home-owner")
            val member = player("home-member")
            val homeWorld = server.worlds.first()
            val faction = faction(owner.uniqueId, member.uniqueId)
            val home = Location(homeWorld, 8.0, 64.0, 8.0)
            val homeKey = home.claimKey()
            GameStateCommands.claim(homeKey, faction.id).await()
            GameStateCommands.setHome(faction.id, io.github.toberocat.improvedfactions.database.storage.HomeSnapshot(
                faction.id, homeWorld.name, home.x, home.y, home.z,
            )).await()
            awaitStorage()
            owner.location = home.clone()
            command("/f unclaim")
                .asPlayer(owner)
                .run()
                .expectHandled()

            await.pollInSameThread().atMost(Duration.ofSeconds(5)).untilAsserted {
                ticks(1)
                assertTrue(StorageManager.cache.claim(homeKey)?.factionId != faction.id)
            }

            assertTrue(checkNotNull(member.nextMessage()).contains("no longer claimed", ignoreCase = true))
            assertTrue(checkNotNull(owner.nextMessage()).contains("no longer claimed", ignoreCase = true))
            assertTrue(checkNotNull(owner.nextMessage()).contains("unclaimed", ignoreCase = true))
            val locationBeforeTeleport = owner.location.clone()

            command("/f home")
                .asPlayer(owner)
                .run()
                .expectHandled()
                .expectMessageContaining("no longer claimed")

            assertNotNull(StorageManager.cache.home(faction.id), "Unclaiming must not delete the stored home")
            assertEquals(locationBeforeTeleport, owner.location, "An unreachable home must not teleport the command sender")
        }
    }

    @Test
    fun `radius unclaim broadcasts a home warning only once`() {
        scenario {
            val owner = player("home-owner")
            val member = player("home-member")
            val homeWorld = server.worlds.first()
            val faction = faction(owner.uniqueId, member.uniqueId)
            val home = Location(homeWorld, 8.0, 64.0, 8.0)
            GameStateCommands.setHome(faction.id, io.github.toberocat.improvedfactions.database.storage.HomeSnapshot(
                faction.id, homeWorld.name, home.x, home.y, home.z,
            )).await()
            (-1..1).forEach { x ->
                (-1..1).forEach { z -> GameStateCommands.claim(io.github.toberocat.improvedfactions.database.storage.ClaimKey(homeWorld.name, x, z), faction.id).await() }
            }
            awaitStorage()

            owner.location = home
            command("/f unclaim 1")
                .asPlayer(owner)
                .run()
                .expectHandled()

            await.pollInSameThread().atMost(Duration.ofSeconds(5)).untilAsserted {
                ticks(1)
                assertTrue(StorageManager.cache.claim(home.claimKey())?.factionId != faction.id)
            }

            assertTrue(checkNotNull(member.nextMessage()).contains("no longer claimed", ignoreCase = true))
            assertEquals(null, member.nextMessage(), "A radius unclaim must broadcast the home warning once")
        }
    }

    @Test
    fun `unclaiming another chunk does not warn faction members about their home`() {
        scenario {
            val owner = player("home-owner")
            val member = player("home-member")
            val homeWorld = server.worlds.first()
            val faction = faction(owner.uniqueId, member.uniqueId)
            val home = Location(homeWorld, 8.0, 64.0, 8.0)
            val otherClaim = Location(homeWorld, 24.0, 64.0, 8.0)
            GameStateCommands.claim(home.claimKey(), faction.id).await()
            GameStateCommands.claim(otherClaim.claimKey(), faction.id).await()
            GameStateCommands.setHome(faction.id, io.github.toberocat.improvedfactions.database.storage.HomeSnapshot(
                faction.id, homeWorld.name, home.x, home.y, home.z,
            )).await()
            awaitStorage()

            owner.location = otherClaim
            command("/f unclaim")
                .asPlayer(owner)
                .run()
                .expectHandled()
                .awaitStorage()

            assertEquals(null, member.nextMessage(), "Unclaiming a non-home chunk must not warn the faction")
            assertNotNull(StorageManager.cache.home(faction.id), "Unclaiming another chunk must retain the stored home")
        }
    }

    @Test
    fun `reclaimed stored home teleports only after countdown and movement cancels without success`() {
        scenario {
            val owner = player("home-owner")
            val homeWorld = server.worlds.first()
            val faction = faction(owner.uniqueId)
            val home = Location(homeWorld, 8.0, 64.0, 8.0)
            val homeKey = home.claimKey()
            GameStateCommands.setHome(faction.id, io.github.toberocat.improvedfactions.database.storage.HomeSnapshot(
                faction.id, homeWorld.name, home.x, home.y, home.z,
            )).await()
            GameStateCommands.claim(homeKey, faction.id).await()
            awaitStorage()
            await.pollInSameThread().atMost(Duration.ofSeconds(5)).untilAsserted {
                ticks(1)
                assertNotNull(StorageManager.cache.home(faction.id))
                assertEquals(faction.id, StorageManager.cache.claim(homeKey)?.factionId)
            }

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
}
