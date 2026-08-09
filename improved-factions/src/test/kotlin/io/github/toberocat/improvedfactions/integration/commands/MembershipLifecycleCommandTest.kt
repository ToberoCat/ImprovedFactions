package io.github.toberocat.improvedfactions.integration.commands

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.cachedUser
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Durable membership promises exercised through the complete Bukkit command pipeline.
 *
 * These assertions intentionally use persisted cache state. They remain meaningful if command
 * processors, argument parsers, or generated contracts are regenerated.
 */
class MembershipLifecycleCommandTest : FactionsIntegrationTest() {
    @Test
    fun `confirmed ownership transfer promotes target and demotes previous owner to default rank`() {
        val owner = player("Owner")
        val successor = player("Successor")
        val faction = faction(owner.uniqueId, successor.uniqueId)

        command("/f transferowner ${successor.name} confirm")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .awaitStorage()
            .expectDeclaredResponse("transferowner", "ownershipTransferred")

        val updatedFaction = checkNotNull(StorageManager.cache.faction(faction.id))
        assertEquals(successor.uniqueId, updatedFaction.owner)
        assertEquals(updatedFaction.defaultRankId, owner.cachedUser().rankId)
        assertTrue(successor.cachedUser().rankId != updatedFaction.defaultRankId)
        assertEquals(faction.id, owner.cachedUser().factionId)
        assertEquals(faction.id, successor.cachedUser().factionId)
    }

    @Test
    fun `expired invite is neither listed nor accepted through commands`() {
        val owner = player("Owner")
        val recruit = player("Recruit")
        val faction = faction(owner.uniqueId)
        GameStateCommands.createInvite(
            owner.uniqueId,
            recruit.uniqueId,
            faction.id,
            faction.defaultRankId,
            Instant.now().minusSeconds(1),
        ).await()
        awaitStorage()

        command("/f invites")
            .asPlayer(recruit)
            .run()
            .expectHandled()
            .expectDeclaredResponse("invites", "noInvites")

        command("/f inviteaccept ${faction.name}")
            .asPlayer(recruit)
            .run()
            .expectHandled(false)
            .expectLocalizedResponse("base.arguments.faction-invite.not-invited")
            .awaitStorage()

        assertTrue(StorageManager.cache.invites(recruit.uniqueId).isEmpty())
        assertEquals(-1, recruit.cachedUser().factionId)
    }
}
