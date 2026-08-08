package io.github.toberocat.improvedfactions.integration.commands.invite

import org.mockbukkit.mockbukkit.entity.PlayerMock
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import java.time.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InviteAcceptCommandTest : ImprovedFactionsTest() {

    private lateinit var player1: PlayerMock
    private lateinit var faction: FactionSnapshot

    @BeforeEach
    override fun setUp() {
        super.setUp()
        player1 = createTestPlayer()
        player1.isOp = true

        faction = testFaction(player1.uniqueId)
    }

    @ParameterizedTest
    @CsvSource(
        "true, Hello_World",
        "false, Hello_World",
        "true, _Player123",
        "false, _Player123",
        "true, TestPlayer_",
        "false, TestPlayer_"
    )
    fun `test if can accept invite with _ in name`(onlineMode: Boolean, playerName: String) {
        server.onlineMode = onlineMode

        val testPlayer = createTestPlayer(playerName)
        GameStateCommands.createInvite(
            player1.uniqueId,
            testPlayer.uniqueId,
            faction.id,
            faction.defaultRankId,
            Instant.now().plusSeconds(300)
        )
        awaitStorage()
        assertTrue(server.dispatchCommand(testPlayer, "f inviteaccept ${faction.name}"))
        awaitStorage()
        assertNotNull(testPlayer.nextMessage())
    }
}
