package io.github.toberocat.improvedfactions.modules.dynmap.handles

import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position

interface FactionDynmapModuleHandle {
    fun factionClusterChange(cluster: Cluster)
    fun factionClaimRemove(position: Position)
}