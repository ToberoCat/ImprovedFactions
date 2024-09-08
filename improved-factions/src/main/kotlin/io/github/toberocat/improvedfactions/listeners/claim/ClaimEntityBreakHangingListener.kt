package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.hanging.HangingBreakByEntityEvent

class ClaimEntityBreakHangingListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace(): String = "entity-break-hanging"

    @EventHandler
    fun hangingBreak(event: HangingBreakByEntityEvent) {
        protectChunk(event, event.entity, event.remover as? Player ?: return)
    }
}