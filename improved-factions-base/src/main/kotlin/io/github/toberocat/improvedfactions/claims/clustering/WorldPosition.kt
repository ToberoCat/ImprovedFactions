package io.github.toberocat.improvedfactions.claims.clustering

data class WorldPosition(val world: String, val x: Int, val y: Int) {
    fun angleWithPivot(p1: WorldPosition): Double {
        return kotlin.math.atan2((p1.y - y).toDouble(), (p1.x - x).toDouble())
    }
}