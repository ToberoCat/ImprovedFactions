package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityChangeBlockEvent

class ClaimMobDestructionListener(zoneType: String) : ProtectionListener(zoneType) {

    override fun namespace(): String = "mob-destruction"


    @EventHandler
    fun onMobDestruction(event: EntityChangeBlockEvent) {
        if (event.entity !is Monster) return
        protectChunk(event, event.block.chunk)
    }
}