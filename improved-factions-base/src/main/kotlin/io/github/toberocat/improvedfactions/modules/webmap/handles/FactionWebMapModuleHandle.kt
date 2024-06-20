package io.github.toberocat.improvedfactions.modules.webmap.handles

import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.ChunkPosition
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Location

interface FactionWebMapModuleHandle {
    fun factionHomeChange(faction: Faction, homeLocation: Location)
    fun clusterChange(cluster: Cluster)
    fun clusterRemove(cluster: Cluster)
    fun removePosition(position: ChunkPosition)
}