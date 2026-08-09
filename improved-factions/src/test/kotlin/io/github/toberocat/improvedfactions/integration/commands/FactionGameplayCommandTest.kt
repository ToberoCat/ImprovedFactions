package io.github.toberocat.improvedfactions.integration.commands

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.cachedUser
import io.github.toberocat.improvedfactions.database.storage.isInFaction
import io.github.toberocat.improvedfactions.factions.FactionJoinType
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Public gameplay promises exercised through the same Bukkit command boundary players use.
 *
 * These deliberately assert durable state, rather than generated command metadata, so changing
 * a processor, parser, or command contract cannot silently regenerate the expectation away.
 */
class FactionGameplayCommandTest : FactionsIntegrationTest() {
    @Test
    fun `ban cannot remove or ban a member of another faction`() {
        val executor = player("AlphaOwner")
        val target = player("BetaOwner")
        val alpha = faction(executor.uniqueId)
        val betaId = GameStateCommands.createFaction(
            target.uniqueId,
            "BetaFaction",
            50,
            ownerAndMemberRanks(),
            Permissions.knownPermissions.keys,
        ).await()

        command("/f ban ${target.name}")
            .asPlayer(executor)
            .run()
            .expectHandled()
            .expectDeclaredResponse("ban", "notInFaction")
            .awaitStorage()

        assertEquals(betaId, target.cachedUser().factionId)
        assertTrue(StorageManager.cache.bans(alpha.id).none { it.userId == target.cachedUser().id })
    }

    @Test
    fun `kick cannot remove a member of another faction`() {
        val executor = player("AlphaOwner")
        val target = player("BetaOwner")
        faction(executor.uniqueId)
        val betaId = GameStateCommands.createFaction(
            target.uniqueId,
            "BetaFaction",
            50,
            ownerAndMemberRanks(),
            Permissions.knownPermissions.keys,
        ).await()

        command("/f kick ${target.name}")
            .asPlayer(executor)
            .run()
            .expectHandled()
            .expectDeclaredResponse("kick", "invalidMember")
            .awaitStorage()

        assertEquals(betaId, target.cachedUser().factionId)
    }

    @Test
    fun `open faction join adds the player with the faction default rank`() {
        val owner = player("Owner")
        val recruit = player("Recruit")
        val faction = faction(owner.uniqueId)
        GameStateCommands.setJoinType(faction.id, FactionJoinType.OPEN).await()
        awaitStorage()

        command("/f join ${faction.name}")
            .asPlayer(recruit)
            .run()
            .expectHandled()
            .awaitStorage()
            .expectDeclaredResponse("join", "joinedFaction", mapOf("factionName" to faction.name))

        assertEquals(faction.id, recruit.cachedUser().factionId)
        assertEquals(faction.defaultRankId, recruit.cachedUser().rankId)
    }

    @Test
    fun `closed faction join leaves an uninvited player outside`() {
        val owner = player("Owner")
        val recruit = player("Recruit")
        val faction = faction(owner.uniqueId)

        command("/f join ${faction.name}")
            .asPlayer(recruit)
            .run()
            .expectHandled()
            .expectDeclaredResponse("join", "factionNotOpen")
            .awaitStorage()

        assertFalse(recruit.cachedUser().isInFaction())
    }

    @Test
    fun `accepting an invite joins its faction and consumes the invite`() {
        val owner = player("Owner")
        val recruit = player("Recruit")
        val faction = faction(owner.uniqueId)

        command("/f invite ${recruit.name} Member")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .awaitStorage()
            .expectDeclaredResponse("invite", "invitedPlayer", mapOf("player" to recruit.name))
        assertEquals(1, StorageManager.cache.invites(recruit.uniqueId).size)

        command("/f inviteaccept ${faction.name}")
            .asPlayer(recruit)
            .run()
            .expectHandled()
            .awaitStorage()
            .expectDeclaredResponse("inviteaccept", "inviteAccepted", mapOf("factionName" to faction.name))

        assertEquals(faction.id, recruit.cachedUser().factionId)
        assertTrue(StorageManager.cache.invites(recruit.uniqueId).isEmpty())
    }

    @Test
    fun `owner cannot leave even when confirmation is supplied`() {
        val owner = player("Owner")
        val faction = faction(owner.uniqueId)

        command("/f leave confirm")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .expectLocalizedResponse("base.exceptions.player-cant-leave-is-owner")
            .awaitStorage()

        assertEquals(faction.id, owner.cachedUser().factionId)
        assertEquals(owner.uniqueId, StorageManager.cache.faction(faction.id)?.owner)
    }

    @Test
    fun `member leave requires confirmation and only changes membership after confirming`() {
        val owner = player("Owner")
        val member = player("Member")
        val faction = faction(owner.uniqueId, member.uniqueId)

        command("/f leave")
            .asPlayer(member)
            .run()
            .expectHandled()
            .expectDeclaredResponse("leave", "confirmationNeeded", mapOf("command" to "leave"))
            .awaitStorage()
        assertEquals(faction.id, member.cachedUser().factionId)

        command("/f leave confirm")
            .asPlayer(member)
            .run()
            .expectHandled()
            .awaitStorage()
            .expectDeclaredResponse("leave", "factionLeft", mapOf("factionName" to faction.name))

        assertFalse(member.cachedUser().isInFaction())
        assertEquals(owner.uniqueId, StorageManager.cache.faction(faction.id)?.owner)
    }

    private fun ownerAndMemberRanks() = listOf(
        GameStateCommands.DefaultRankSpec(
            "Member", 1, setOf(Permissions.SEND_INVITES, Permissions.VIEW_POWER, Permissions.HOME),
        ),
        GameStateCommands.DefaultRankSpec("Owner", 1000, Permissions.knownPermissions.keys),
    )
}
