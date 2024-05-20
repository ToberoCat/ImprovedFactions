package io.github.toberocat.improvedfactions.claims.clustering

class EdgeBetweenCells(cellFrom: Position, cellTo: Position) {

    val from: Pair<Int, Int>
    val to: Pair<Int, Int>

    init {
        findCommonNodes(cellFrom, cellTo).let { (from, to) ->
            this.from = from
            this.to = to
        }
    }

    private fun Position.getNodes(): Set<Pair<Int, Int>> {
        val (x, y) = this
        return setOf(
            Pair(x, y),
            Pair(x + 1, y),
            Pair(x, y + 1),
            Pair(x + 1, y + 1)
        )
    }

    private fun findCommonNodes(cellFrom: Position, cellTo: Position): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val cellFromNodes = cellFrom.getNodes()
        val cellToNodes = cellTo.getNodes()
        val results = cellFromNodes.intersect(cellToNodes)
        if (results.size != 2)
            throw IllegalArgumentException("Cells must have exactly 2 common nodes to form a valid edge.")
        return results.toList().let { Pair(it[0], it[1]) }
    }
}