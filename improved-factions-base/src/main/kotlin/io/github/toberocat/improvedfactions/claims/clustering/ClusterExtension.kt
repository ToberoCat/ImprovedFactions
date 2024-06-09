package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.bukkit.entity.Player

object ClusterExtension {
    fun Player.getCurrentClusters(chunkRenderDistance: Int) = location.world?.name?.let { world ->
        ChunkPosition(location.chunk.x, location.chunk.z, world)
            .getNeighbours(chunkRenderDistance)
            .mapNotNull { ImprovedFactionsPlugin.instance.claimChunkClusters.getCluster(it) }
    } ?: emptyList()
}