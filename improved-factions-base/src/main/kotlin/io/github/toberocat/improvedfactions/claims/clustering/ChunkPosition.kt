package io.github.toberocat.improvedfactions.claims.clustering

import org.bukkit.Bukkit
import kotlin.math.sqrt

data class ChunkPosition(val x: Int, val y: Int, val world: String) {

    fun distanceSquaredTo(x: Double, y: Double): Double {
        val deltaX = this.x - x
        val deltaY = this.y - y
        return deltaX * deltaX + deltaY * deltaY
    }

    fun distanceTo(other: ChunkPosition): Double {
        return sqrt(distanceSquaredTo(other.x.toDouble(), other.y.toDouble()))
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChunkPosition

        if (x != other.x) return false
        if (y != other.y) return false
        if (world != other.world) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + world.hashCode()
        return result
    }

    fun getCornerPoints() = listOf(
        WorldPosition(world, x * 16, y * 16),
        WorldPosition(world, x * 16 + 16, y * 16),
        WorldPosition(world, x * 16, y * 16 + 16),
        WorldPosition(world, x * 16 + 16, y * 16 + 16)
    )

    fun uniquId() = "$x-$y-$world"
    operator fun minus(cellFrom: ChunkPosition) = ChunkPosition(x - cellFrom.x, y - cellFrom.y, world)
    fun toWorldPosition(): WorldPosition = WorldPosition(world, x * 16, y * 16)
    fun getChunk() = Bukkit.getWorld(world)?.getChunkAt(x, y)
}