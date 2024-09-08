package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class ClaimTramplingListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace() = "trampling"

    @EventHandler
    private fun onTrample(event: PlayerInteractEvent) {
        if (event.action != Action.PHYSICAL) return
        if (event.clickedBlock?.type != Material.FARMLAND) return
        protectChunk(event, event.clickedBlock, event.player)
    }
}