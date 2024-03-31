package io.github.toberocat.improvedfactions.modules.dynmap.handles

import io.github.toberocat.improvedfactions.claims.clustering.Cluster

interface FactionDynmapModuleHandle {
    fun clusterChange(cluster: Cluster)
}