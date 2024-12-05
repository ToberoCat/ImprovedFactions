package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerArmorStandManipulateEvent

class ClaimArmorStandManipulateListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace() = "armor-stand-manipulate"

    @EventHandler
    fun armorStandManipulate(event: PlayerArmorStandManipulateEvent) =
        protectChunk(event, event.rightClicked, event.player)
}