package io.github.toberocat.improvedfactions.integration.commands.home

import org.mockbukkit.mockbukkit.entity.PlayerMock
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.Chunk
import org.bukkit.World
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HomeCommandProcessorTest : ImprovedFactionsTest() {

    private lateinit var player1: PlayerMock
    private lateinit var player2: PlayerMock
    private lateinit var faction: FactionSnapshot
    private lateinit var world: World
    private lateinit var chunk: Chunk

    @BeforeEach
    override fun setUp() {
        super.setUp()
        player1 = createTestPlayer()
        player2 = createTestPlayer()

        faction = testFaction(player1.uniqueId, members = arrayOf(player2.uniqueId))
        world = server.worlds.first()
        BaseModule.config.allowedWorlds = BaseModule.config.allowedWorlds + world.name

        chunk = world.getChunkAt(0, 0)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `members can teleport to their faction home`(onlineMode: Boolean) {
        server.onlineMode = onlineMode

        player1.location = chunk.getBlock(8, 8, 8).location
        assertTrue(server.dispatchCommand(player1, "f claim"))
        awaitStorage()
        assertTrue(server.dispatchCommand(player1, "f sethome"))
        awaitStorage()
        val homeLocation = player1.location.clone()
        assertTrue(server.dispatchCommand(player1, "f home"))
        player2.location = chunk.getBlock(12, 8, 12).location
        assertTrue(server.dispatchCommand(player2, "f home"))
        Thread.sleep(5_100)
        server.scheduler.performTicks(120)

        assertEquals(homeLocation.world, player2.world)
        assertEquals(homeLocation.x, player2.location.x)
        assertEquals(homeLocation.y, player2.location.y)
        assertEquals(homeLocation.z, player2.location.z)
    }

    @Test
    fun `member without home permission is not teleported`() {
        player1.location = chunk.getBlock(8, 8, 8).location
        assertTrue(server.dispatchCommand(player1, "f claim"))
        awaitStorage()
        assertTrue(server.dispatchCommand(player1, "f sethome"))
        awaitStorage()
        GameStateCommands.setPermission(faction.defaultRankId, Permissions.HOME, false)
            .toCompletableFuture().get()
        awaitStorage()

        player2.location = chunk.getBlock(12, 8, 12).location
        val originalLocation = player2.location.clone()
        assertTrue(server.dispatchCommand(player2, "f home"))

        assertEquals(originalLocation, player2.location)
        assertTrue(assertNotNull(player2.nextMessage()).contains("required permission"))
    }
}
