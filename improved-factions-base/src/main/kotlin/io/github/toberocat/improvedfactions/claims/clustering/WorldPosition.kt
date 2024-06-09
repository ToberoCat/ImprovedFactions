package io.github.toberocat.improvedfactions.claims.clustering

import kotlin.math.sign
import kotlin.math.sqrt

data class WorldPosition(val world: String, val x: Int, val y: Int) {
    fun isSameAxis(other: WorldPosition) = x == other.x && y != other.y || x != other.x && y == other.y

    fun isSameAxis(other: WorldPosition, allowedAxis: Set<Pair<Int, Int>>) =
        allowedAxis.contains(getAxis(other))

    private fun distanceSquaredTo(x: Double, y: Double): Double {
        val deltaX = this.x - x
        val deltaY = this.y - y
        return deltaX * deltaX + deltaY * deltaY
    }

    fun computeChunksFromCenter() = toChunkPosition().run {
        listOf(
            ChunkPosition(x, y, world),
            ChunkPosition(x - 1, y, world),
            ChunkPosition(x - 1, y - 1, world),
            ChunkPosition(x, y - 1, world)
        )
    }

    fun distanceTo(other: WorldPosition) = sqrt(distanceSquaredTo(other.x.toDouble(), other.y.toDouble()))

    private fun toChunkPosition() = ChunkPosition(x shr 4, y shr 4, world)

    fun getAxis(other: WorldPosition): Pair<Int, Int> {
        val axisX = x - other.x
        val axisY = y - other.y
        return Pair(
            sign(axisX.toDouble()).toInt() * if (axisX != 0) 1 else 0,
            sign(axisY.toDouble()).toInt() * if (axisY != 0) 1 else 0
        )
    }
}