package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.claimKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class InFactionPvPListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace(): String = "in-faction-pvp"

    @EventHandler
    fun pvp(event: EntityDamageByEntityEvent) {
        val damaged = event.entity as? Player
        val damager = event.damager as? Player
        if (damaged == null || damager == null) return
        if (!StorageManager.cache.isReady()) {
            event.isCancelled = true
            return
        }
        if (StorageManager.cache.claim(damaged.location.claimKey())?.zoneType != zoneType) return
        val damagerFaction = StorageManager.cache.user(damager.uniqueId)?.factionId
        val damagedFaction = StorageManager.cache.user(damaged.uniqueId)?.factionId
        if (damagerFaction == null || damagerFaction != damagedFaction) return
        event.isCancelled = true
    }
}
