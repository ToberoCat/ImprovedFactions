package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent

class ClaimBlockBreakListener(zoneType: String, sendMessage: Boolean = true) : ProtectionListener(zoneType, sendMessage) {

    override fun namespace(): String = "block-break"

    @EventHandler
    fun placeEvent(event: BlockBreakEvent) = protectChunk(event, event.block, event.player)
}
