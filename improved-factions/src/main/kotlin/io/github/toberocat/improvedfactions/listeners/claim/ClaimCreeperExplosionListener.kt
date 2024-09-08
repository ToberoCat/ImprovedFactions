package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityExplodeEvent

class ClaimCreeperExplosionListener(zoneType: String) : ProtectionListener(zoneType) {

    override fun namespace(): String = "creeper-explosion"

    @EventHandler
    fun onCreeperExplosion(event: EntityExplodeEvent) {
        if (event.entityType != EntityType.CREEPER) return
        protectChunk(event, event.entity.location.chunk)
    }
}