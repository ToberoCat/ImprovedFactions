package io.github.toberocat.improvedfactions.claims.clustering

class ClusterReachabilityChecker(private val positions: Set<Position>) {
    private val visited = mutableSetOf<Position>()

    fun getUnreachablePositions(): Set<Position> {
        if (positions.isEmpty())
            return emptySet()
        val start = positions.first()
        dfs(start)
        return positions.toSet().subtract(visited)
    }

    private fun dfs(position: Position) {
        if (position in visited)
            return

        visited.add(position)
        getNeighbors(position).forEach(::dfs)
    }

    private fun getNeighbors(position: Position) = listOf(
        Position(position.x - 1, position.y, position.world),
        Position(position.x + 1, position.y, position.world),
        Position(position.x, position.y + 1, position.world),
        Position(position.x, position.y - 1, position.world)
    ).filter { it in positions }
}