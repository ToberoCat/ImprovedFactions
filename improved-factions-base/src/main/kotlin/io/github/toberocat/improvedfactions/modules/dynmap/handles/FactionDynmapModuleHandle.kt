package io.github.toberocat.improvedfactions.modules.dynmap.handles

import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Location

interface FactionDynmapModuleHandle {
    fun factionHomeChange(faction: Faction, homeLocation: Location)
    fun clusterChange(cluster: Cluster)
    fun clusterRemove(cluster: Cluster)
}