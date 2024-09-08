package io.github.toberocat.improvedfactions.claims.clustering.query

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.database.DatabaseManager
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or

class DatabaseClaimQueryProvider : ClaimQueryProvider {
    override fun queryNeighbours(claim: FactionClaim) = DatabaseManager.loggedTransaction {
        return@loggedTransaction FactionClaim.find {
            FactionClaims.world eq claim.world and (
                    FactionClaims.chunkX eq claim.chunkX - 1 and (FactionClaims.chunkZ eq claim.chunkZ) or
                            (FactionClaims.chunkX eq claim.chunkX + 1 and (FactionClaims.chunkZ eq claim.chunkZ)) or
                            (FactionClaims.chunkZ eq claim.chunkZ - 1 and (FactionClaims.chunkX eq claim.chunkX)) or
                            (FactionClaims.chunkZ eq claim.chunkZ + 1 and (FactionClaims.chunkX eq claim.chunkX))
                    )
        }.toList()
    }

    override fun allFactionPositions() = DatabaseManager.loggedTransaction {
        FactionClaim.find { FactionClaims.factionId neq noFactionId }
            .map { it to it.factionId }
    }

    override fun allZonePositions() = DatabaseManager.loggedTransaction {
        return@loggedTransaction FactionClaim.find {
            FactionClaims.factionId eq noFactionId and
                    (FactionClaims.zoneType neq ZoneHandler.FACTION_ZONE_TYPE)
        }.map { it to it.zoneType }
    }
}