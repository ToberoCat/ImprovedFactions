package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ClaimProjectileListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace() = "projectiles"

    @EventHandler
    fun onProjectileDamage(event: EntityDamageByEntityEvent) {
        val projectile = event.damager as? Projectile ?: return
        val player = projectile.shooter as? Player ?: return
        protectChunk(event, event.entity, player)
    }
}
