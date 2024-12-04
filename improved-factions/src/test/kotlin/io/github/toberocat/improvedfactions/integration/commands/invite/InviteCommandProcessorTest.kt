package io.github.toberocat.improvedfactions.integration.commands.invite

import be.seeseemelk.mockbukkit.entity.PlayerMock
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
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
}