package io.github.toberocat.improvedfactions.modules.dynmap.handles

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.clustering.cluster.Cluster
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Location

class DummyFactionDynmapModuleHandles : FactionDynmapModuleHandle {
    override fun factionHomeChange(faction: Faction, homeLocation: Location) = Unit
    override fun clusterChange(cluster: Cluster) = Unit
    override fun clusterRemove(cluster: Cluster) = Unit
    override fun removeClaim(position: FactionClaim) = Unit
}