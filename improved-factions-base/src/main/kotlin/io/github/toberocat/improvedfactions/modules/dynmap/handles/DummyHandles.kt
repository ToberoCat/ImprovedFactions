package io.github.toberocat.improvedfactions.modules.dynmap.handles

import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.ChunkPosition
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Location

class DummyFactionDynmapModuleHandles : FactionDynmapModuleHandle {
    override fun factionHomeChange(faction: Faction, homeLocation: Location) = Unit
    override fun clusterChange(cluster: Cluster) = Unit
    override fun clusterRemove(cluster: Cluster) = Unit
    override fun removePosition(position: ChunkPosition) = Unit
}