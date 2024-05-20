package io.github.toberocat.improvedfactions.modules.power.handles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.FactionCluster
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Chunk
import org.bukkit.configuration.file.FileConfiguration


class DummyFactionPowerRaidModuleHandle : FactionPowerRaidModuleHandle {
    override fun memberJoin(faction: Faction) = Unit
    override fun memberLeave(faction: Faction) = Unit
    override fun claimChunk(chunk: Chunk, faction: Faction) = Unit
    override fun calculateUnprotectedChunks(cluster: FactionCluster, unprotectedPositions: MutableSet<Position>) = Unit

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) = Unit
}