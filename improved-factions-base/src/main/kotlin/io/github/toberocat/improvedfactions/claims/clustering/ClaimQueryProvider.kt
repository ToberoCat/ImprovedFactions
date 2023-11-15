package io.github.toberocat.improvedfactions.claims.clustering

interface ClaimQueryProvider {
    fun querySameFactionNeighbours(position: Position) =
        queryNeighbours(position).filter { it.factionId == position.factionId }

    fun queryNeighbours(position: Position): List<Position>
    fun all(): List<Position>
}
