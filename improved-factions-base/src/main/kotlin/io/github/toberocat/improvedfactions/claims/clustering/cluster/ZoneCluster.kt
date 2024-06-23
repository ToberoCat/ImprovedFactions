package io.github.toberocat.improvedfactions.claims.clustering.cluster

import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import java.util.*

class ZoneCluster(val zoneType: String, id: UUID, positions: MutableSet<ChunkPosition>) : Cluster(id, positions) {
    override fun scheduleUpdate() = Unit
    override fun getColor(): Int = ZoneHandler.getZone(zoneType)?.mapColor ?: 0xffffff
}