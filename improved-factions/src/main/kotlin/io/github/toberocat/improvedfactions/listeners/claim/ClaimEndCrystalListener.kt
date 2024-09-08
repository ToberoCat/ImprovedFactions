package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityPlaceEvent

class ClaimEndCrystalListener(zoneType: String, sendMessage: Boolean = true) :
    ProtectionListener(zoneType, sendMessage) {
    override fun namespace() = "entity-place"

    @EventHandler
    private fun onEndCrystal(event: EntityPlaceEvent) {
        event.player?.let { protectChunk(event, event.entity, it) }
    }
}