package io.github.toberocat.improvedfactions.integration.commands.invite

import be.seeseemelk.mockbukkit.entity.PlayerMock
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InviteAcceptCommandTest : ImprovedFactionsTest() {

    private lateinit var player1: PlayerMock
    private lateinit var faction: Faction

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
        transaction { faction.invite(player1.uniqueId, testPlayer.uniqueId, faction.getDefaultRank().id.value) }
        assertTrue(server.dispatchCommand(testPlayer, "f inviteaccept ${faction.name}"))
        assertNotNull(testPlayer.nextMessage())
    }
}