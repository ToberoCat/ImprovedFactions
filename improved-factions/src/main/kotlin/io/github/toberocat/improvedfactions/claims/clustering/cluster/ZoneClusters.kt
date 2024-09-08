package io.github.toberocat.improvedfactions.claims.clustering.cluster

import org.jetbrains.exposed.dao.id.IntIdTable

object ZoneClusters : IntIdTable("zone_clusters") {
    val zoneType = varchar("zone_type", 255)
}