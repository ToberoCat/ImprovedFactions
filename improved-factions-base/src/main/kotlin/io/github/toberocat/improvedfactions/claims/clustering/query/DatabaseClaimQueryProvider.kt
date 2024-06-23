package io.github.toberocat.improvedfactions.claims.clustering.query

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.database.DatabaseManager
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or

class DatabaseClaimQueryProvider : ClaimQueryProvider {
    override fun queryNeighbours(position: ChunkPosition) = DatabaseManager.loggedTransaction {
        return@loggedTransaction FactionClaim.find {
            FactionClaims.world eq position.world and (
                    FactionClaims.chunkX eq position.x - 1 and (FactionClaims.chunkZ eq position.y) or
                            (FactionClaims.chunkX eq position.x + 1 and (FactionClaims.chunkZ eq position.y)) or
                            (FactionClaims.chunkZ eq position.y - 1 and (FactionClaims.chunkX eq position.x)) or
                            (FactionClaims.chunkZ eq position.y + 1 and (FactionClaims.chunkX eq position.x))
                    )
        }.map { ChunkPosition(it.chunkX, it.chunkZ, it.world) }
    }

    override fun allFactionPositions() = DatabaseManager.loggedTransaction {
        FactionClaim.find { FactionClaims.factionId neq noFactionId }
            .map { ChunkPosition(it.chunkX, it.chunkZ, it.world) to it.factionId }
    }

    override fun allZonePositions() = DatabaseManager.loggedTransaction {
        return@loggedTransaction FactionClaim.find {
            FactionClaims.factionId eq noFactionId and
                    (FactionClaims.zoneType neq ZoneHandler.FACTION_ZONE_TYPE)
        }.map { ChunkPosition(it.chunkX, it.chunkZ, it.world) to it.zoneType }
    }
}