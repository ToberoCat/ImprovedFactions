package io.github.toberocat.improvedfactions.claims.clustering

data class Position(val x: Int, val y: Int, val world: String) {


    fun distanceSquaredTo(x: Double, y: Double): Double {
        val deltaX = this.x - x
        val deltaY = this.y - y
        return deltaX * deltaX + deltaY * deltaY
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

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

    fun uniquId() = "$x-$y-$world"
    operator fun minus(cellFrom: Position) = Position(x - cellFrom.x, y - cellFrom.y, world)
}