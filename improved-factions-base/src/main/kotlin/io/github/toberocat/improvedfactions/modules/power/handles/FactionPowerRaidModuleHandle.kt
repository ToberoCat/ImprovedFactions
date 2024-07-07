package io.github.toberocat.improvedfactions.modules.power.handles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.clustering.cluster.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Chunk

interface FactionPowerRaidModuleHandle {
    fun memberJoin(faction: Faction)
    fun memberLeave(faction: Faction)
    fun claimChunk(chunk: Chunk, faction: Faction)
    fun calculateUnprotectedChunks(cluster: Cluster): Set<FactionClaim>
    fun reloadConfig(plugin: ImprovedFactionsPlugin)
}