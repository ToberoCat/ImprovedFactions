package io.github.toberocat.improvedfactions.claims.clustering.query

import io.github.toberocat.improvedfactions.claims.FactionClaim

interface ClaimQueryProvider {
    fun queryNeighbours(claim: FactionClaim): List<FactionClaim>
    fun allFactionPositions(): List<Pair<FactionClaim, Int>>
    fun allZonePositions(): List<Pair<FactionClaim, String>>
}