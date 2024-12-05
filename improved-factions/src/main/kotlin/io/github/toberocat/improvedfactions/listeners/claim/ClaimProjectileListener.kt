package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ProjectileHitEvent

class ClaimProjectileListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace() = "projectiles"

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        val player = event.entity.shooter as? Player ?: return
        protectChunk(event, event.entity, player)
    }
}