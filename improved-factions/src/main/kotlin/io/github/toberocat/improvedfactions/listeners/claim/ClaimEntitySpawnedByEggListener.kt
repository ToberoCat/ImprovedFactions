package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent

class ClaimEntitySpawnedByEggListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace(): String = "entity-spawned-by-egg"

    @EventHandler
    fun spawn(event: PlayerInteractEvent) {
        if (!event.material.name.contains("SPAWN_EGG")) return
        protectChunk(event, event.clickedBlock, event.player)
    }
}