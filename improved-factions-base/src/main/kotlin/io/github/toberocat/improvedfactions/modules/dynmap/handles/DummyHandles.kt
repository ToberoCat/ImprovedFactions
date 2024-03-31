package io.github.toberocat.improvedfactions.modules.dynmap.handles

import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position

class DummyFactionDynmapModuleHandles : FactionDynmapModuleHandle {
    override fun factionClusterChange(cluster: Cluster) = Unit

    override fun factionClaimRemove(position: Position) = Unit
}