package io.github.toberocat.improvedfactions.integration.commands.invite

import org.mockbukkit.mockbukkit.entity.PlayerMock
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InviteCommandProcessorTest : ImprovedFactionsTest() {

    private lateinit var player1: PlayerMock
    private lateinit var player2: PlayerMock
    private lateinit var faction: Faction

    @BeforeEach
    override fun setUp() {
        super.setUp()
        player1 = createTestPlayer()
        player2 = createTestPlayer()
        player1.isOp = true
        player2.isOp = true

        faction = testFaction(player1.uniqueId)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `test if can invite player`(onlineMode: Boolean) {
        server.onlineMode = onlineMode

        assertTrue(server.dispatchCommand(player1, "f invite ${player2.name} Member"))
        assertNotNull(player1.nextMessage())
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
    fun `test if can invite player with _`(onlineMode: Boolean, playerName: String) {
        server.onlineMode = onlineMode

        val testPlayer = createTestPlayer(playerName)
        assertTrue(server.dispatchCommand(player1, "f invite ${testPlayer.name} Member"))
        assertNotNull(player1.nextMessage())
    }
}