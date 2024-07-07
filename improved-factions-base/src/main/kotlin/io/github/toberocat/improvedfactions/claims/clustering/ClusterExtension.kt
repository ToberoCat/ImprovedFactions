package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import org.bukkit.entity.Player

fun Player.getCurrentClusters(chunkRenderDistance: Int) = location.world?.name?.let { world ->
    ChunkPosition(location.chunk.x, location.chunk.z, world)
        .getNeighbours(chunkRenderDistance)
        .mapNotNull { it.getFactionClaim() }
        .mapNotNull { ImprovedFactionsPlugin.instance.claimChunkClusters.getCluster(it) }
} ?: emptyList()
