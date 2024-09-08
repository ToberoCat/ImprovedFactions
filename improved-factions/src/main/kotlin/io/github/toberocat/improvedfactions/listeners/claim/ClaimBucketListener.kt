package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketEntityEvent
import org.bukkit.event.player.PlayerBucketFillEvent

class ClaimBucketListener(zoneType: String, sendMessage: Boolean = true) : ProtectionListener(zoneType, sendMessage) {
    override fun namespace() = "bucket-usage"

    @EventHandler
    private fun onBucket(event: PlayerBucketEmptyEvent) = protectChunk(event, event.block, event.player)

    @EventHandler
    private fun onBucket(event: PlayerBucketFillEvent) = protectChunk(event, event.block, event.player)

    @EventHandler
    private fun onBucket(event: PlayerBucketEntityEvent) = protectChunk(event, event.entity, event.player)
}