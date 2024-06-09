package io.github.toberocat.improvedfactions.claims.clustering

interface ClaimQueryProvider {
    fun queryNeighbours(position: ChunkPosition): List<ChunkPosition>
    fun allFactionPositions(): List<Pair<ChunkPosition, Int>>
    fun allZonePositions(): List<Pair<ChunkPosition, String>>
}
