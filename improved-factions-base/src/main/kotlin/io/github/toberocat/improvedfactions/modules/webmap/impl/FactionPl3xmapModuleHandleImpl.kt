package io.github.toberocat.improvedfactions.modules.webmap.impl

import io.github.toberocat.improvedfactions.claims.clustering.ChunkPosition
import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.webmap.handles.FactionWebMapModuleHandle
import org.bukkit.Location

class FactionPl3xmapModuleHandleImpl : FactionWebMapModuleHandle {
    override fun factionHomeChange(faction: Faction, homeLocation: Location) {
        TODO("Not yet implemented")
    }

    override fun clusterChange(cluster: Cluster) {
        TODO("Not yet implemented")
    }

    override fun clusterRemove(cluster: Cluster) {
        TODO("Not yet implemented")
    }

    override fun removePosition(position: ChunkPosition) {
        TODO("Not yet implemented")
    }
}