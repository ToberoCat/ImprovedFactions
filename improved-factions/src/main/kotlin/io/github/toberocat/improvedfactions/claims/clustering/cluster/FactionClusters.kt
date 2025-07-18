package io.github.toberocat.improvedfactions.claims.clustering.cluster

import io.github.toberocat.improvedfactions.factions.Factions
import org.jetbrains.exposed.dao.id.IntIdTable
import java.util.*

object FactionClusters : IntIdTable("faction_clusters") {
    val faction = reference("faction", Factions)
    // val parentClusterId = uuid("parent_cluster").default(UUID.randomUUID()) // <-- This is previous version
    val parentClusterId = uuid("parent_cluster").clientDefault { UUID.randomUUID() } // <-- Mysql Fix
}
