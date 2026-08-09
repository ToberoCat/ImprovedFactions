package io.github.toberocat.improvedfactions.integration.commands

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.cachedUser
import io.github.toberocat.improvedfactions.modules.relations.RelationType
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Security and relation semantics exercised through Bukkit rather than processor calls. */
class RankAndRelationSecurityCommandTest : FactionsIntegrationTest() {
    @Test
    fun `manager cannot create or grant an equal or higher rank`() {
        val owner = player("Owner")
        val manager = player("Manager")
        val member = player("Member")
        val factionId = GameStateCommands.createFaction(
            owner.uniqueId,
            "RankFaction",
            50,
            listOf(
                GameStateCommands.DefaultRankSpec("Member", 1, emptySet()),
                GameStateCommands.DefaultRankSpec("Manager", 10, setOf(Permissions.MANAGE_PERMISSIONS)),
                GameStateCommands.DefaultRankSpec("Owner", 1000, Permissions.knownPermissions.keys),
            ),
            Permissions.knownPermissions.keys,
        ).await()
        awaitStorage()
        val managerRank = StorageManager.cache.ranks(factionId).single { it.name == "Manager" }
        val memberRank = StorageManager.cache.ranks(factionId).single { it.name == "Member" }
        val ownerRank = StorageManager.cache.ranks(factionId).single { it.name == "Owner" }
        GameStateCommands.setUserFaction(manager.uniqueId, factionId, managerRank.id).await()
        GameStateCommands.setUserFaction(member.uniqueId, factionId, memberRank.id).await()
        awaitStorage()

        command("/f rank create Escalation 10")
            .asPlayer(manager).run().expectHandled().expectDeclaredResponse("rank create", "noPermission")
        command("/f rank assign ${member.name} Owner")
            .asPlayer(manager).run().expectHandled().expectDeclaredResponse("rank assign", "noPermission")
        command("/f rank default Owner")
            .asPlayer(manager).run().expectHandled().expectDeclaredResponse("rank default", "noPermission")
        command("/f rank set Owner manage-claims false")
            .asPlayer(manager).run().expectHandled().expectDeclaredResponse("rank set", "noPermission")
        command("/f rank delete Owner Member")
            .asPlayer(manager).run().expectHandled().expectDeclaredResponse("rank delete", "noPermission")
        awaitStorage()

        val faction = checkNotNull(StorageManager.cache.faction(factionId))
        assertEquals(memberRank.id, member.cachedUser().rankId)
        assertEquals(memberRank.id, faction.defaultRankId)
        assertNotNull(StorageManager.cache.rank(ownerRank.id))
        assertTrue(Permissions.MANAGE_CLAIMS in checkNotNull(StorageManager.cache.rank(ownerRank.id)).permissions)
        assertTrue(StorageManager.cache.ranks(factionId).none { it.name == "Escalation" })
    }

    @Test
    fun `accepting an old invite cannot move a player out of their current faction`() {
        val alphaOwner = player("AlphaOwner")
        val betaOwner = player("BetaOwner")
        val recruit = player("Recruit")
        val alpha = faction(alphaOwner.uniqueId)

        command("/f invite ${recruit.name} Member")
            .asPlayer(alphaOwner)
            .run()
            .expectHandled()
            .awaitStorage()
        assertNotNull(alphaOwner.nextComponentMessage())
        assertNotNull(recruit.nextComponentMessage())
        assertEquals(1, StorageManager.cache.invites(recruit.uniqueId).size)

        val betaId = GameStateCommands.createFaction(
            betaOwner.uniqueId,
            "BetaFaction",
            50,
            ownerAndMemberRanks(),
            Permissions.knownPermissions.keys,
        ).await()
        awaitStorage()
        val beta = checkNotNull(StorageManager.cache.faction(betaId))
        GameStateCommands.setUserFaction(recruit.uniqueId, beta.id, beta.defaultRankId).await()
        awaitStorage()
        command("/f inviteaccept ${alpha.name}")
            .asPlayer(recruit)
            .run()
            .expectHandled()
            .expectDeclaredResponse("inviteaccept", "alreadyInFaction")
            .awaitStorage()

        assertEquals(beta.id, recruit.cachedUser().factionId)
        assertEquals(1, StorageManager.cache.invites(recruit.uniqueId).size)
    }

    @Test
    fun `ownership cannot be transferred to the current owner`() {
        val owner = player("Owner")
        val faction = faction(owner.uniqueId)
        val originalRankId = owner.cachedUser().rankId

        command("/f transferowner ${owner.name} confirm")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .expectDeclaredResponse("transferowner", "cantTransferToSelf")
            .awaitStorage()

        assertEquals(owner.uniqueId, checkNotNull(StorageManager.cache.faction(faction.id)).owner)
        assertEquals(originalRankId, owner.cachedUser().rankId)
    }

    @Test
    fun `alliance is symmetric and a conflicting war command is localized without mutating it`() {
        val alphaOwner = player("AlphaOwner")
        val betaOwner = player("BetaOwner")
        val alpha = faction(alphaOwner.uniqueId)
        val beta = GameStateCommands.createFaction(
            betaOwner.uniqueId,
            "BetaFaction",
            50,
            ownerAndMemberRanks(),
            Permissions.knownPermissions.keys,
        ).await()
        awaitStorage()

        command("/f ally BetaFaction")
            .asPlayer(alphaOwner)
            .run()
            .expectHandled()
            .awaitStorage()
            .expectDeclaredResponse("ally", "allyInviteSuccess")
        command("/f allyaccept ${alpha.name}")
            .asPlayer(betaOwner).run().expectHandled().awaitStorage()

        assertTrue(beta in StorageManager.cache.relations(alpha.id, RelationType.ALLY.name))
        assertTrue(alpha.id in StorageManager.cache.relations(beta, RelationType.ALLY.name))

        command("/f war BetaFaction")
            .asPlayer(alphaOwner)
            .run()
            .expectHandled(false)
            .expectLocalizedResponse("relations.exceptions.already-allied")
            .awaitStorage()

        assertTrue(beta in StorageManager.cache.relations(alpha.id, RelationType.ALLY.name))
        assertFalse(beta in StorageManager.cache.relations(alpha.id, RelationType.ENEMY.name))
    }

    private fun ownerAndMemberRanks() = listOf(
        GameStateCommands.DefaultRankSpec("Member", 1, setOf(Permissions.SEND_INVITES, Permissions.VIEW_POWER, Permissions.HOME)),
        GameStateCommands.DefaultRankSpec("Owner", 1000, Permissions.knownPermissions.keys),
    )
}
