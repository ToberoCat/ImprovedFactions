package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.user.noFactionId
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or

class DatabaseClaimQueryProvider : ClaimQueryProvider {
    override fun queryNeighbours(position: Position) = loggedTransaction {
        FactionClaim.find {
            FactionClaims.factionId neq noFactionId and
                    (FactionClaims.chunkX inList listOf(position.x + 1, position.x - 1)) and
                    (FactionClaims.chunkZ inList listOf(position.y + 1, position.y - 1)) and
                    (FactionClaims.chunkX neq position.x or (FactionClaims.chunkZ neq position.y))
        }.map { Position(it.chunkX, it.chunkZ, it.world, it.factionId) }
            .toList()
    }

    override fun all() = loggedTransaction {
        FactionClaim.find { FactionClaims.factionId neq noFactionId }
            .map { Position(it.chunkX, it.chunkZ, it.world, it.factionId) }
    }
}