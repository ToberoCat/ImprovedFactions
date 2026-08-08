package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.claimKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class GeneralPvPListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace(): String = "general-pvp"

    @EventHandler
    fun pvp(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player
            || event.damager !is Player
        ) return
        if (!StorageManager.cache.isReady()) {
            event.isCancelled = true
            return
        }
        if (StorageManager.cache.claim(event.entity.location.claimKey())?.zoneType != zoneType) return
        protectChunk(event, event.entity, event.damager as Player)
    }
}
