package io.github.toberocat.improvedfactions.modules.power.handles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.FactionCluster
import io.github.toberocat.improvedfactions.claims.clustering.ChunkPosition
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Chunk

interface FactionPowerRaidModuleHandle {
    fun memberJoin(faction: Faction)
    fun memberLeave(faction: Faction)
    fun claimChunk(chunk: Chunk, faction: Faction)
    fun calculateUnprotectedChunks(cluster: FactionCluster, unprotectedPositions: MutableSet<ChunkPosition>)
    fun reloadConfig(plugin: ImprovedFactionsPlugin)
}