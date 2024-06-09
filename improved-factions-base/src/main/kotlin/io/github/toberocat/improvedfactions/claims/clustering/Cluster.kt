package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.utils.LazyUpdate
import java.util.UUID

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

    private fun updateCluster() {
        calculateCenter()
        outerNodes.scheduleUpdate()
        DynmapModule.dynmapModule().dynmapModuleHandle.clusterChange(this)
    }

    private fun calculateCenter() {
        if (positions.isEmpty())
            return

        centerX = positions.map { it.x }.average()
        centerY = positions.map { it.y }.average()
    }

    private fun detectOuterNodes(): List<List<WorldPosition>> {
        val openChunks = positions.toMutableList()

        fun WorldPosition.allowedAxis(): Set<Pair<Int, Int>> {
            val possibleCornerPoints = computeChunksFromCenter().toSet()
                .subtract(positions)
                .flatMap { it.getCornerPoints() }
                .filter { it.isSameAxis(this) }
            return possibleCornerPoints.map { it.getAxis(this) }.toSet()
        }

        val cornerPoints = mutableMapOf<WorldPosition, Int>()
        while (openChunks.isNotEmpty())
            openChunks.removeFirst()
                .getCornerPoints()
                .forEach { cornerPoints[it] = cornerPoints.getOrDefault(it, 0) + 1 }
        val openSupportingPoints = cornerPoints.filter { it.value == 1 || it.value == 3 }.keys.toMutableList()
        val outerNodes = mutableListOf<List<WorldPosition>>()
        while (openSupportingPoints.isNotEmpty()) {
            val loop = mutableListOf<WorldPosition>()
            var next = openSupportingPoints.firstOrNull()
            while (next != null) {
                loop.add(next)
                openSupportingPoints.remove(next)

                val allowedAxis = next.allowedAxis()
                next = openSupportingPoints.filter { it.isSameAxis(next!!, allowedAxis) }.minByOrNull { it.distanceTo(next!!) }
            }
            loop.add(loop.first()) // Close the polygon
            outerNodes.add(loop)
        }
        return outerNodes
    }
}