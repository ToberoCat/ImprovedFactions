package io.github.toberocat.improvedfactions.claims.clustering

interface ClaimQueryProvider {
    fun queryNeighbours(position: Position): List<Position>
    fun allFactionPositions(): List<Pair<Position, Int>>
    fun allZonePositions(): List<Pair<Position, String>>
}
