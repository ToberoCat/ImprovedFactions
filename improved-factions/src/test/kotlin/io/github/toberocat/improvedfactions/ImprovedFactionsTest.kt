package io.github.toberocat.improvedfactions

import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.testing.IntegrationTest
import io.github.toberocat.improvedfactions.testing.CommandTestInvocation
import org.bukkit.Material
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import org.mockbukkit.mockbukkit.world.WorldMock
import org.bukkit.command.CommandSender
import org.mockbukkit.mockbukkit.entity.PlayerMock
import java.util.UUID
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit

/** MockBukkit fixture for tests that exercise the plugin as a running system. */
@IntegrationTest
open class FactionsIntegrationTest {
    lateinit var server: ServerMock
    lateinit var plugin: ImprovedFactionsPlugin

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

    fun player(name: String? = null) = server.addPlayer().also {
        it.isOp = true

        if (name != null) {
            it.name = name
        }
    }

    fun world(name: String? = null) = WorldMock(Material.DIRT, 3).also {
        if (name != null) it.name = name
        BaseModule.config.allowedWorlds += it.name
    }

    fun faction(owner: UUID = UUID.randomUUID(), vararg members: UUID): FactionSnapshot {
        val ranks = listOf(
            GameStateCommands.DefaultRankSpec(
                "Member", 1, setOf(Permissions.SEND_INVITES, Permissions.VIEW_POWER, Permissions.HOME)
            ),
            GameStateCommands.DefaultRankSpec("Owner", 1000, Permissions.knownPermissions.keys)
        )
        val factionId = GameStateCommands.createFaction(owner, "TestFaction", 50, ranks, Permissions.knownPermissions.keys)
            .await()
        val faction = checkNotNull(StorageManager.cache.faction(factionId))
        members.forEach {
            GameStateCommands.setUserFaction(it, factionId, faction.defaultRankId)
                .await()
        }
        return checkNotNull(StorageManager.cache.faction(factionId))
    }

    fun execute(sender: CommandSender, command: String): Boolean =
        server.dispatchCommand(sender, command.removePrefix("/"))

    fun command(input: String) = CommandTestInvocation(this, input)

    fun ticks(count: Long = 1) {
        require(count >= 0) { "count must not be negative" }
        server.scheduler.performTicks(count)
    }

    fun scenario(block: FactionsTestScenario.() -> Unit): FactionsTestScenario =
        FactionsTestScenario(this).apply(block)

    fun <T> CompletionStage<T>.await(timeoutSeconds: Long = 5): T =
        toCompletableFuture().get(timeoutSeconds, TimeUnit.SECONDS)

    fun createTestPlayer(name: String? = null) = player(name)

    fun testWorld(name: String? = null) = world(name)

    fun testFaction(owner: UUID = UUID.randomUUID(), vararg members: UUID) = faction(owner, *members)

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
            StorageManager.requestRefresh()?.await()
        }
        ticks(2)
    }
}

class FactionsTestScenario internal constructor(private val fixture: FactionsIntegrationTest) {
    val server: ServerMock get() = fixture.server
    val plugin: ImprovedFactionsPlugin get() = fixture.plugin

    fun player(name: String? = null): PlayerMock = fixture.player(name)
    fun world(name: String? = null): WorldMock = fixture.world(name)
    fun faction(owner: UUID = UUID.randomUUID(), vararg members: UUID): FactionSnapshot =
        fixture.faction(owner, *members)
    fun execute(sender: CommandSender, command: String): Boolean = fixture.execute(sender, command)
    fun awaitStorage() = fixture.awaitStorage()
    fun ticks(count: Long = 1) = fixture.ticks(count)
}

/** Kept as a source-compatible bridge for the existing tests. */
open class ImprovedFactionsTest : FactionsIntegrationTest()
