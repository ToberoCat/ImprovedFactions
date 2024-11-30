package io.github.toberocat.improvedfactions.modules.power.handles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.clustering.cluster.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Chunk


class DummyFactionPowerRaidModuleHandle : FactionPowerRaidModuleHandle {
    override fun memberJoin(faction: Faction) = Unit
    override fun memberLeave(faction: Faction) = Unit
    override fun claimChunk(chunk: Chunk, faction: Faction) = Unit
    override fun calculateUnprotectedChunks(cluster: Cluster) = emptySet<FactionClaim>()

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) = Unit
    override fun getActivePowerAccumulation(faction: Faction) = 0.0
    override fun getInactivePowerAccumulation(faction: Faction) = 0.0
    override fun getClaimMaintenanceCost(faction: Faction) = 0.0

    override fun getPowerAccumulated(activeAccumulation: Double, inactiveAccumulation: Double) = 0.0
    override fun getNextClaimCost(faction: Faction) = 0
}