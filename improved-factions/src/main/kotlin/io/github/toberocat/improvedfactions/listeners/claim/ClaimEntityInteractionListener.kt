package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEntityEvent

class ClaimEntityInteractionListener(zoneType: String) : ProtectionListener(
    zoneType,
    sendMessage = false
) {
    override fun namespace(): String = "entity-interaction"

    @EventHandler
    fun interactWithEntity(event: PlayerInteractEntityEvent) = protectChunk(event, event.rightClicked, event.player)
}