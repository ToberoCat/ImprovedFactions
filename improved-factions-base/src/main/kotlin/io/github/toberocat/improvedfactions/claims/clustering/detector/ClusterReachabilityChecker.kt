package io.github.toberocat.improvedfactions.claims.clustering.detector

import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition

class ClusterReachabilityChecker(private val positions: Set<ChunkPosition>) {
    private val visited = mutableSetOf<ChunkPosition>()

    fun getUnreachablePositions(): Set<ChunkPosition> {
        if (positions.isEmpty())
            return emptySet()
        val start = positions.first()
        dfs(start)
        return positions.toSet().subtract(visited)
    }

    private fun dfs(position: ChunkPosition) {
        if (position in visited)
            return

        visited.add(position)
        getNeighbors(position).forEach(::dfs)
    }

    private fun getNeighbors(position: ChunkPosition) = listOf(
        ChunkPosition(position.x - 1, position.y, position.world),
        ChunkPosition(position.x + 1, position.y, position.world),
        ChunkPosition(position.x, position.y + 1, position.world),
        ChunkPosition(position.x, position.y - 1, position.world)
    ).filter { it in positions }
}