package io.github.toberocat.improvedfactions.claims.clustering.cluster

import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.claims.clustering.position.WorldPosition
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.utils.LazyUpdate
import java.util.*

abstract class Cluster(val id: UUID, private val positions: MutableSet<ChunkPosition>) {
    private val outerNodes = LazyUpdate(mutableListOf()) { detectOuterNodes() }

    private val world = positions.firstOrNull()?.world
        ?: throw IllegalArgumentException("Cluster must have at least one position")

    var centerX = 0.0
    var centerY = 0.0

    init {
        updateCluster()
    }

    abstract fun scheduleUpdate()
    abstract fun getColor(): Int

    fun getOuterNodes() = outerNodes.get()

    fun getReadOnlyPositions(): Set<ChunkPosition> = positions.toSet()

    fun isEmpty() = positions.isEmpty()

    fun removeAll(position: Set<ChunkPosition>) {
        positions.removeAll(position)
        updateCluster()
    }

    fun addAll(positions: Set<ChunkPosition>) {
        if (positions.any { it.world != this.world })
            throw IllegalArgumentException("All positions must belong to the same world")

        this.positions.addAll(positions)
        updateCluster()
    }

    private fun calculateCenter() {
        if (positions.isEmpty())
            return

        centerX = positions.map { it.x }.average()
        centerY = positions.map { it.y }.average()
    }
}