package io.github.toberocat.improvedfactions.claims.clustering

import java.util.*

class ZoneCluster(val zoneType: String, id: UUID, positions: MutableSet<Position>) : Cluster(id, positions) {
    override fun scheduleUpdate() = Unit
}