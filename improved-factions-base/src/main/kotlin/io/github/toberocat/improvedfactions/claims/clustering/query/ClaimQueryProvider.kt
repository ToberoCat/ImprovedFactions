package io.github.toberocat.improvedfactions.claims.clustering.query

import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition

interface ClaimQueryProvider {
    fun queryNeighbours(position: ChunkPosition): List<ChunkPosition>
    fun allFactionPositions(): List<Pair<ChunkPosition, Int>>
    fun allZonePositions(): List<Pair<ChunkPosition, String>>
}