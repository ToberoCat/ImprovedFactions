package io.github.toberocat.improvedfactions.integration.commands.create

import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.database.storage.cachedUser
import io.github.toberocat.improvedfactions.database.storage.isInFaction
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockbukkit.mockbukkit.entity.PlayerMock
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

        assertFalse(player1.cachedUser().isInFaction())
        assertTrue(server.dispatchCommand(player1, "f create TestFaction"))
        awaitStorage()
        assertTrue(player1.cachedUser().isInFaction())
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `fail if in faction`(onlineMode: Boolean) {
        server.onlineMode = onlineMode

        assertFalse(player1.cachedUser().isInFaction())
        assertTrue(server.dispatchCommand(player1, "f create TestFaction"))
        awaitStorage()
        assertTrue(player1.cachedUser().isInFaction())

        assertTrue(server.dispatchCommand(player1, "f create FailMe"))
        awaitStorage()
        assertTrue(player1.cachedUser().isInFaction())
        assertTrue(StorageManager.cache.factions().size == 1)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `fail with invalid name`(onlineMode: Boolean) {
        server.onlineMode = onlineMode

        assertFalse(player1.cachedUser().isInFaction())
        assertTrue(server.dispatchCommand(player1, "f create Hi There"))
        assertTrue(server.dispatchCommand(player1, "f create <Nope>"))
        assertTrue(server.dispatchCommand(player1, "f create Some-Name"))
        assertTrue(server.dispatchCommand(player1, "f create Still5Disallowed"))
        assertFalse(player1.cachedUser().isInFaction())
    }
}
