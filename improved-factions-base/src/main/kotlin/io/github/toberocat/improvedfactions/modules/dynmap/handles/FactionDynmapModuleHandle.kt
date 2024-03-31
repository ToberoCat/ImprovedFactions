package io.github.toberocat.improvedfactions.modules.dynmap.handles

import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.zone.Zone

interface FactionDynmapModuleHandle {
    fun factionClusterChange(cluster: Cluster)
    fun factionClaimRemove(position: Position)
    fun zoneClaimAdd(zone: Zone, position: Position)
    fun zoneClaimRemove(position: Position)
}