package io.github.toberocat.improvedfactions.integration.commands.create

import be.seeseemelk.mockbukkit.entity.PlayerMock
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.user.factionUser
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreateCommandTest : ImprovedFactionsTest() {

    private lateinit var player1: PlayerMock

    @BeforeEach
    override fun setUp() {
        super.setUp()
        player1 = createTestPlayer()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `creating a faction`(onlineMode: Boolean) {
        server.onlineMode = onlineMode

        assertFalse(player1.factionUser().isInFaction())
        assertTrue(server.dispatchCommand(player1, "f create TestFaction"))
        assertTrue(player1.factionUser().isInFaction())
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `fail if in faction`(onlineMode: Boolean) {
        server.onlineMode = onlineMode

        transaction {
            assertFalse(player1.factionUser().isInFaction())
            assertTrue(server.dispatchCommand(player1, "f create TestFaction"))
            assertTrue(player1.factionUser().isInFaction())

            assertTrue(server.dispatchCommand(player1, "f create FailMe"))
            assertTrue(player1.factionUser().isInFaction())
            assertTrue(FactionHandler.getFactions().count() == 1L)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `fail with invalid name`(onlineMode: Boolean) {
        server.onlineMode = onlineMode

        assertFalse(player1.factionUser().isInFaction())
        assertTrue(server.dispatchCommand(player1, "f create Hi There"))
        assertTrue(server.dispatchCommand(player1, "f create <Nope>"))
        assertTrue(server.dispatchCommand(player1, "f create Some-Name"))
        assertTrue(server.dispatchCommand(player1, "f create Still5Disallowed"))
        assertFalse(player1.factionUser().isInFaction())
    }
}