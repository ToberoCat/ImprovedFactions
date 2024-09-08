package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockPlaceEvent

class ClaimBlockPlaceListener(zoneType: String) : ProtectionListener(zoneType) {

    override fun namespace(): String = "block-place"

    @EventHandler
    fun placeEvent(event: BlockPlaceEvent) = protectChunk(event, event.blockPlaced, event.player)
}