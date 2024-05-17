package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent

class ClaimBlockInteractListener(zoneType: String) : ProtectionListener(
    zoneType,
    sendMessage = false
) {
    override fun namespace(): String = "block-interaction"

    @EventHandler
    fun interact(event: PlayerInteractEvent) {
        if (event.clickedBlock?.type?.isInteractable == false)
            return
        protectChunk(event, event.clickedBlock, event.player)
    }
}