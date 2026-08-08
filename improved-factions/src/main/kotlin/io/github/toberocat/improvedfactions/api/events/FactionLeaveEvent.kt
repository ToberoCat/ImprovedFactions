package io.github.toberocat.improvedfactions.api.events

import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.UserSnapshot
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Fired when a player leaves a faction.
 */
class FactionLeaveEvent(val faction: FactionSnapshot, val user: UserSnapshot) : Event(), Cancellable {
    private var isCancelled = false

    override fun getHandlers(): HandlerList = handlerList
    
    override fun isCancelled(): Boolean = isCancelled

    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
