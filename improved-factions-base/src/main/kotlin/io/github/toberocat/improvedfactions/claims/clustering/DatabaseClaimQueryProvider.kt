package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.user.noFactionId
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or

class DatabaseClaimQueryProvider : ClaimQueryProvider {
    override fun queryNeighbours(position: Position) = loggedTransaction {
        val claims = FactionClaim.find {
            (FactionClaims.world eq position.world) and (
                    (FactionClaims.chunkX eq position.x - 1 and (FactionClaims.chunkZ eq position.y)) or
                            (FactionClaims.chunkX eq position.x + 1 and (FactionClaims.chunkZ eq position.y)) or
                            (FactionClaims.chunkZ eq position.y - 1 and (FactionClaims.chunkX eq position.x)) or
                            (FactionClaims.chunkZ eq position.y + 1 and (FactionClaims.chunkX eq position.x))
                    )
        }
        return@loggedTransaction claims.map { Position(it.chunkX, it.chunkZ, it.world, it.factionId) }
    }


    override fun all() = loggedTransaction {
        FactionClaim.find { FactionClaims.factionId neq noFactionId }
            .map { Position(it.chunkX, it.chunkZ, it.world, it.factionId) }
    }
}