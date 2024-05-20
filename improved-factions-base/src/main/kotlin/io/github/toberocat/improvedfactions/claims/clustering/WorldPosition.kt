package io.github.toberocat.improvedfactions.claims.clustering

import kotlin.math.sqrt

data class WorldPosition(val world: String, val x: Int, val y: Int) {
    fun distanceTo(other: WorldPosition): Double {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt(dx * dx + dy * dy.toDouble())
    }

}