package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.claims.FactionClaims
import org.jetbrains.exposed.dao.id.UUIDTable

object Clusters : UUIDTable("clusters") {
    val centerX = double("centerX")
    val centerY = double("centerY")
    val world = varchar("world", 255)
    val type = enumeration("type", ClusterType::class)

    val centerLazyUpdate = bool("centerLazyUpdate").default(false)
    val isOuterNodeLazyUpdate = FactionClaims.bool("outerNodeLazyUpdate").default(false)
}