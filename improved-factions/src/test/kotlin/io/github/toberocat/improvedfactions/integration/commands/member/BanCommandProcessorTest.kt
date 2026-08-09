package io.github.toberocat.improvedfactions.integration.commands.member

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.cachedUser
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BanCommandProcessorTest : ImprovedFactionsTest() {
    @Test
    fun `banning a member of another faction does not remove them from their faction`() {
        val executor = createTestPlayer("Executor")
        val target = createTestPlayer("Target")
        testFaction(executor.uniqueId)
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

        assertTrue(server.dispatchCommand(executor, "f ban ${target.name}"))
        awaitStorage()

        assertEquals(targetFactionId, target.cachedUser().factionId)
    }
}
