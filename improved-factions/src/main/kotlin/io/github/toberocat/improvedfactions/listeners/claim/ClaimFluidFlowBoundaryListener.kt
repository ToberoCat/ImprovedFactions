package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.database.storage.claimKey
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockFromToEvent

/** Optionally stops water and lava only when they flow into a protected claim. */
class ClaimFluidFlowBoundaryListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace() = "fluid-flow-boundary"

    @EventHandler
    fun onFluidFlow(event: BlockFromToEvent) {
        if (crossesClaimBoundary(event.block, event.toBlock) && shouldProtect(event.toBlock.chunk)) event.isCancelled = true
    }

    private fun crossesClaimBoundary(source: org.bukkit.block.Block, destination: org.bukkit.block.Block): Boolean {
        if (source.claimKey() == destination.claimKey()) return false
        val sourceClaim = StorageManager.cache.claim(source.claimKey())
        val destinationClaim = StorageManager.cache.claim(destination.claimKey())
        return sourceClaim?.factionId != destinationClaim?.factionId || sourceClaim?.zoneType != destinationClaim?.zoneType
    }
}
