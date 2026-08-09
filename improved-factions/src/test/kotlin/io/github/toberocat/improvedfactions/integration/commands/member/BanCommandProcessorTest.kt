package io.github.toberocat.improvedfactions.integration.commands.member

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.cachedUser
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BanCommandProcessorTest : FactionsIntegrationTest() {
    @Test
    fun `ban cannot remove or ban a member of another faction`() {
        val executor = createTestPlayer("Executor")
        val target = createTestPlayer("Target")
        val executorFaction = testFaction(executor.uniqueId)
        val targetFactionId = GameStateCommands.createFaction(
            target.uniqueId,
            "TargetFaction",
            50,
            listOf(
                GameStateCommands.DefaultRankSpec("Member", 1, setOf(Permissions.SEND_INVITES)),
                GameStateCommands.DefaultRankSpec("Owner", 1000, Permissions.knownPermissions.keys)
            ),
            Permissions.knownPermissions.keys
        ).toCompletableFuture().get(5, TimeUnit.SECONDS)

        command("/f ban ${target.name}")
            .asPlayer(executor)
            .run()
            .expectHandled()
            .expectDeclaredResponse("ban", "notInFaction")
            .awaitStorage()

        assertEquals(targetFactionId, target.cachedUser().factionId)
        assertTrue(StorageManager.cache.bans(executorFaction.id).none { it.userId == target.cachedUser().id })
    }

    @Test
    fun `ban removes and records a member of the executor faction`() {
        val executor = createTestPlayer("Executor")
        val target = createTestPlayer("Target")
        val executorFaction = testFaction(executor.uniqueId, target.uniqueId)

        command("/f ban ${target.name}")
            .asPlayer(executor)
            .run()
            .expectHandled()
            .awaitStorage()
            .expectDeclaredResponse("ban", "bannedTarget", mapOf("target" to target.name))

        assertEquals(-1, target.cachedUser().factionId)
        assertTrue(StorageManager.cache.bans(executorFaction.id).any { it.userId == target.cachedUser().id })
    }
}
