package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.FactionClaims
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseClaimQueryProvider : ClaimQueryProvider {
    override fun queryNeighbours(position: Position) = transaction {
        FactionClaim.find {
            FactionClaims.chunkX inList listOf(position.x + 1, position.x - 1) and
                    (FactionClaims.chunkZ inList listOf(position.y + 1, position.y - 1)) and
                    (FactionClaims.chunkX neq position.x or (FactionClaims.chunkZ neq position.y))
        }.map { Position(it.chunkX, it.chunkZ, it.factionId) }
            .toList()
    }

    override fun all() = transaction { FactionClaim.all().map { Position(it.chunkX, it.chunkZ, it.factionId) } }
}