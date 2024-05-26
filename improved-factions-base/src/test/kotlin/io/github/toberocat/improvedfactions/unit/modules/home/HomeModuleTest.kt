package io.github.toberocat.improvedfactions.unit.modules.home

import be.seeseemelk.mockbukkit.WorldMock
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.home.HomeModule.setHome
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.Location
import org.bukkit.Material
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class HomeModuleTest : ImprovedFactionsTest() {
    @Test
    fun `test home creation in claim`() {
        val world = testWorld()
        val chunk = world.getChunkAt(0, 0)

        val faction = testFaction()
        transaction {
            faction.claim(chunk)
            assertDoesNotThrow { faction.setHome(chunk.getBlock(0, 0, 0).location) }
        }
    }

    @Test
    fun `no homes allowed outside of claimed regions`() {
        val faction = testFaction()
        val world = testWorld()
        transaction {
            assertThrows<CommandException> { faction.setHome(Location(world, 0.0, 0.0, 0.0)) }
        }
    }
}