package io.github.toberocat.improvedfactions.integration.api

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.api.events.*
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.junit.jupiter.api.Test
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.*

class FactionEventsTest : ImprovedFactionsTest() {
    @Test
    fun `create command fires immutable event on main thread`() {
        val player = createTestPlayer("Owner")
        val captured = AtomicReference<FactionCreateEvent>()
        val onMain = AtomicBoolean(false)
        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onCreate(event: FactionCreateEvent) {
                captured.set(event)
                onMain.set(Bukkit.isPrimaryThread())
            }
        }, plugin)

        assertTrue(server.dispatchCommand(player, "f create TestFaction"))
        awaitStorage()

        assertEquals(player.uniqueId, captured.get().ownerId)
        assertEquals("TestFaction", captured.get().factionName)
        assertTrue(onMain.get())
        assertEquals("TestFaction", player.cachedUser().faction()?.name)
    }

    @Test
    fun `cancelled create event prevents storage mutation`() {
        val player = createTestPlayer("Owner")
        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onCreate(event: FactionCreateEvent) = event.setCancelled(true)
        }, plugin)

        assertTrue(server.dispatchCommand(player, "f create CancelledFaction"))
        awaitStorage()

        assertNull(StorageManager.cache.factions().firstOrNull { it.name == "CancelledFaction" })
        assertEquals(noFactionId, player.cachedUser().factionId)
    }

    @Test
    fun `join leave and delete events carry snapshot DTOs and are cancellable`() {
        val ownerId = UUID.randomUUID()
        val memberId = UUID.randomUUID()
        val faction = FactionSnapshot(7, "SnapshotFaction", ownerId, "OPEN", 20, 50, 1, 3)
        val user = UserSnapshot(memberId, faction.id, "Member", 9, 3, 1, setOf("send-invites"))
        val handled = mutableListOf<String>()
        server.pluginManager.registerEvents(object : Listener {
            @EventHandler fun onJoin(event: FactionJoinEvent) {
                assertSame(faction, event.faction); assertSame(user, event.user)
                event.setCancelled(true); handled += "join"
            }
            @EventHandler fun onLeave(event: FactionLeaveEvent) {
                assertSame(faction, event.faction); assertSame(user, event.user)
                event.setCancelled(true); handled += "leave"
            }
            @EventHandler fun onDelete(event: FactionDeleteEvent) {
                assertSame(faction, event.faction)
                event.setCancelled(true); handled += "delete"
            }
        }, plugin)

        val join = FactionJoinEvent(faction, user).also(Bukkit.getPluginManager()::callEvent)
        val leave = FactionLeaveEvent(faction, user).also(Bukkit.getPluginManager()::callEvent)
        val delete = FactionDeleteEvent(faction).also(Bukkit.getPluginManager()::callEvent)

        assertTrue(join.isCancelled)
        assertTrue(leave.isCancelled)
        assertTrue(delete.isCancelled)
        assertEquals(listOf("join", "leave", "delete"), handled)
    }
}
