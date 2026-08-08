package io.github.toberocat.improvedfactions

import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.Material
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import org.mockbukkit.mockbukkit.world.WorldMock
import java.util.UUID
import java.util.concurrent.TimeUnit

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
        MockBukkit.unmock()
    }

    fun createTestPlayer(name: String? = null) = server.addPlayer().also {
        it.isOp = true

        if (name != null) {
            it.name = name
        }
    }

    fun testWorld(name: String? = null) = WorldMock(Material.DIRT, 3).also {
        if (name != null) it.name = name
        BaseModule.config.allowedWorlds += it.name
    }

    fun testFaction(owner: UUID = UUID.randomUUID(), vararg members: UUID): FactionSnapshot {
        val ranks = listOf(
            GameStateCommands.DefaultRankSpec("Member", 1, setOf(Permissions.SEND_INVITES)),
            GameStateCommands.DefaultRankSpec("Owner", 1000, Permissions.knownPermissions.keys)
        )
        val factionId = GameStateCommands.createFaction(owner, "TestFaction", 50, ranks, Permissions.knownPermissions.keys)
            .toCompletableFuture().get(5, TimeUnit.SECONDS)
        val faction = checkNotNull(StorageManager.cache.faction(factionId))
        members.forEach {
            GameStateCommands.setUserFaction(it, factionId, faction.defaultRankId)
                .toCompletableFuture().get(5, TimeUnit.SECONDS)
        }
        return checkNotNull(StorageManager.cache.faction(factionId))
    }

    /** Legacy clustering fixtures only; runtime code never calls the Exposed entity model. */
    internal fun testLegacyFaction(owner: UUID = UUID.randomUUID(), id: Int): Faction {
        return transaction {
            Faction.findById(id) ?: Faction.new(id) {
                this.owner = owner
                name = "TestFaction$id"
            }
        }
    }

    /** Tests may block; production command/event paths must never do so. */
    fun awaitStorage() {
        repeat(2) {
            StorageManager.requestRefresh()?.toCompletableFuture()?.get(5, TimeUnit.SECONDS)
        }
        server.scheduler.performTicks(2)
    }

}
