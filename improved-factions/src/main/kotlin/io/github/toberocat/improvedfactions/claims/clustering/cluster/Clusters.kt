package io.github.toberocat.improvedfactions.claims.clustering.cluster

import io.github.toberocat.improvedfactions.claims.clustering.ClusterType
import org.jetbrains.exposed.dao.id.UUIDTable

object Clusters : UUIDTable("clusters") {
    val centerX = double("centerX").default(0.0)
    val centerY = double("centerY").default(0.0)
    val world = varchar("world", 255)
    val type = enumeration("type", ClusterType::class)
    val typeReferenceId = integer("type_reference_id")

    val centerLazyUpdate = bool("center_lazy_update").default(false)
}