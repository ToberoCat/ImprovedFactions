package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.CreatureSpawnEvent

class ClaimMobSpawnListener(zoneType: String) : ProtectionListener(zoneType) {

    override fun namespace(): String = "mob-spawn"

    @EventHandler
    fun onMobSpawn(event: CreatureSpawnEvent) {
        if (event.entity !is Monster) return
        protectChunk(event, event.entity.location.chunk)
    }
}