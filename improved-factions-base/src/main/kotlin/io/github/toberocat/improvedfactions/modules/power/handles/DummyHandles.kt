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
}