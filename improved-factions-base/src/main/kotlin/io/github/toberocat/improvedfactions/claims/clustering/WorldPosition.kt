package io.github.toberocat.improvedfactions.claims.clustering

import kotlin.math.sqrt

data class WorldPosition(val world: String, val x: Int, val y: Int) {
    fun distanceTo(other: WorldPosition): Double {
        val dx = (x - other.x).toDouble()
        val dy = (y - other.y).toDouble()
        return sqrt(dx * dx + dy * dy)
    }

    fun angleWithPivot(p1: WorldPosition): Double {
        return kotlin.math.atan2((p1.y - y).toDouble(), (p1.x - x).toDouble())
    }

    fun distanceSquared(p2: WorldPosition): Int {
        return (x - p2.x) * (x - p2.x) + (y - p2.y) * (y - p2.y)
    }

    fun ccw(p2: WorldPosition, p3: WorldPosition): Int {
        return (p2.x - x) * (p3.y - p2.y) - (p2.y - y) * (p3.x - p2.x)
    }

}