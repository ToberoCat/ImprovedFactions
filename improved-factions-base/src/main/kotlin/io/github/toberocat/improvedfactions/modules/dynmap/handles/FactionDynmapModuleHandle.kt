package io.github.toberocat.improvedfactions.modules.dynmap.handles

import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.zone.Zone
import org.bukkit.Location

interface FactionDynmapModuleHandle {
    fun factionHomeChange(faction: Faction, homeLocation: Location)
    fun factionClusterChange(cluster: Cluster)
    fun factionClaimRemove(position: Position)
    fun zoneClaimAdd(type: String, position: Position)
    fun zoneClaimRemove(position: Position)
}