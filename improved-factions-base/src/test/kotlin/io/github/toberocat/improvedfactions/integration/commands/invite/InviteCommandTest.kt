package io.github.toberocat.improvedfactions.integration.commands.invite

import be.seeseemelk.mockbukkit.entity.PlayerMock
import io.github.toberocat.improvedfactions.commands.invite.InviteCommand
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertTrue

class InviteCommandTest : ImprovedFactionsTest() {

    private lateinit var player1: PlayerMock
    private lateinit var player2: PlayerMock
    private lateinit var faction: Faction

    override fun setUp() {
        super.setUp()
        player1 = createTestPlayer("aaa")
        player2 = createTestPlayer("Tober0")
        player1.isOp = true
        player2.isOp = true

        faction = testFaction(player1.uniqueId)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `test if can invite player`(onlineMode: Boolean) {
        server.onlineMode = onlineMode

        assertTrue(server.dispatchCommand(player1, "f invite ${player2.name} Member"))
        player2.assertSaid("Hello world!");
        player2.assertNoMoreSaid();
    }
}