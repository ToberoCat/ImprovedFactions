package io.github.toberocat.improvedfactions.unit

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.WorldMock
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import org.bukkit.Material
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.util.*


open class ImprovedFactionsTest {
    protected lateinit var server: ServerMock
    protected lateinit var plugin: ImprovedFactionsPlugin

    @BeforeEach
    open fun setUp() {
        System.setProperty("bstats.relocatecheck", "false")
        server = MockBukkit.mock()
        plugin = MockBukkit.load(ImprovedFactionsPlugin::class.java)
    }

    @AfterEach
    fun tearDown() {
        plugin.adventure.close()
        MockBukkit.unmock()
    }

    fun createTestPlayer() = server.addPlayer().also { it.isOp = true }

    fun testWorld(name: String? = null) = WorldMock(Material.DIRT, 3).also {
        if (name != null) it.name = name
        plugin.improvedFactionsConfig.allowedWorlds += it.name
    }

    fun testFaction(owner: UUID = UUID.randomUUID(), id: Int? = null, vararg members: UUID): Faction {
        var faction = transaction { id?.let { FactionHandler.getFaction(it) } }
        if (faction != null) {
            return faction
        }
        faction = FactionHandler.createFaction(owner, "TestFaction", id)
        transaction { members.forEach { faction.join(it, faction.defaultRank) } }
        return faction
    }

}