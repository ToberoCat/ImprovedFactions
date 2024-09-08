package io.github.toberocat.improvedfactions.integration.commands.home

import be.seeseemelk.mockbukkit.entity.PlayerMock
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import org.bukkit.Chunk
import org.bukkit.World
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertTrue

class HomeCommandTest : ImprovedFactionsTest() {

    private lateinit var player1: PlayerMock
    private lateinit var player2: PlayerMock
    private lateinit var faction: Faction
    private lateinit var world: World
    private lateinit var chunk: Chunk

    override fun setUp() {
        super.setUp()
        player1 = createTestPlayer()
        player2 = createTestPlayer()

        faction = testFaction(player1.uniqueId, members = arrayOf(player2.uniqueId))
        world = testWorld()
        FactionClaims.allowedWorlds = setOf(world.name)

        chunk = world.getChunkAt(0, 0)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `illegal state exception not scheduled yet`(onlineMode: Boolean) {
        server.onlineMode = onlineMode

        player1.location = chunk.getBlock(8, 8, 8).location
        assertTrue(server.dispatchCommand(player1, "f claim"))
        assertTrue(server.dispatchCommand(player1, "f sethome"))
        assertTrue(server.dispatchCommand(player1, "f home"))
        assertTrue(server.dispatchCommand(player2, "f home"))
    }
}