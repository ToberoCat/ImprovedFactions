package io.github.toberocat.improvedfactions.modules.dynmap.handles

import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.zone.Zone
import org.bukkit.Location

class DummyFactionDynmapModuleHandles : FactionDynmapModuleHandle {
    override fun factionHomeChange(faction: Faction, homeLocation: Location) = Unit
    override fun factionClusterChange(cluster: Cluster) = Unit
    override fun factionClaimRemove(position: Position) = Unit
    override fun zoneClaimAdd(type: String, position: Position) = Unit
    override fun zoneClaimRemove(position: Position) = Unit
}