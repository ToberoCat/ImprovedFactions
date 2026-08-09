package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.database.storage.claimKey
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockPistonExtendEvent
import org.bukkit.event.block.BlockPistonRetractEvent

/**
 * Optionally stops piston movement only when its destination crosses into a
 * protected claim. Piston movement inside a claim is intentionally unaffected.
 */
class ClaimPistonBoundaryListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace() = "piston-boundary"

    @EventHandler
    fun onExtend(event: BlockPistonExtendEvent) =
        protectDestinations(event, event.blocks.map { it to it.getRelative(event.direction) })

    @EventHandler
    fun onRetract(event: BlockPistonRetractEvent) =
        protectDestinations(event, event.blocks.map { it to it.getRelative(event.direction.oppositeFace) })

    private fun protectDestinations(event: org.bukkit.event.Cancellable, movements: List<Pair<Block, Block>>) {
        if (movements.any { (source, destination) -> crossesClaimBoundary(source, destination) && shouldProtect(destination.chunk) }) {
            event.isCancelled = true
        }
    }

    private fun crossesClaimBoundary(source: Block, destination: Block): Boolean {
        if (source.claimKey() == destination.claimKey()) return false
        val sourceClaim = StorageManager.cache.claim(source.claimKey())
        val destinationClaim = StorageManager.cache.claim(destination.claimKey())
        return sourceClaim?.factionId != destinationClaim?.factionId || sourceClaim?.zoneType != destinationClaim?.zoneType
    }
}
