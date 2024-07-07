package io.github.toberocat.improvedfactions.claims

import io.github.toberocat.improvedfactions.claims.clustering.cluster.Clusters
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.jetbrains.exposed.dao.id.IntIdTable

object FactionClaims : IntIdTable("faction_claims") {
    const val MAX_ZONE_NAME_LENGTH = 25
    private const val MAX_WORLD_NAME_LENGTH = 50
    var allowedWorlds: Set<String> = emptySet()

    val chunkX = integer("chunk_x")
    val chunkZ = integer("chunk_z")
    val factionId = integer("faction_id")
    val world = varchar("world", MAX_WORLD_NAME_LENGTH).default("world")
    val zoneType = varchar("zone_type", MAX_ZONE_NAME_LENGTH).default(ZoneHandler.FACTION_ZONE_TYPE)
    val clusterId = reference("cluster_id", Clusters).nullable()
}