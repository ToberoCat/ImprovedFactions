package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule.Companion.powerRaidModule
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.utils.LazyUpdate

class Cluster(private val positions: MutableSet<Position>) {
    private val powerModuleHandle: FactionPowerRaidModuleHandle = powerRaidModule().factionModuleHandle
    private val outerNodes = LazyUpdate(mutableListOf()) { detectOuterNodes() }
    private val unprotectedPositions = LazyUpdate(mutableSetOf()) {
        mutableSetOf<Position>().apply {
            powerModuleHandle.calculateUnprotectedChunks(this@Cluster, this)
        }
    }

    private val world = positions.firstOrNull()?.world
        ?: throw IllegalArgumentException("Cluster must have at least one position")
    val factionId = positions.firstOrNull()?.factionId
        ?: throw IllegalArgumentException("Cluster must have at least one position")

    var centerX = 0.0
    var centerY = 0.0

    init {
        updateCluster()
    }

    fun getOuterNodes() = outerNodes.get()

    fun getReadOnlyPositions(): Set<Position> = positions.toSet()

    fun scheduleUpdate() {
        unprotectedPositions.scheduleUpdate()
    }

    fun isUnprotected(x: Int, y: Int, world: String): Boolean {
        return Position(x, y, world, -1) in unprotectedPositions.get()
    }

    fun isEmpty() = positions.isEmpty()

    fun removeAll(position: Set<Position>) {
        positions.removeAll(position)
        updateCluster()
    }

    fun addAll(positions: Set<Position>) {
        if (positions.any { it.factionId != factionId })
            throw IllegalArgumentException("All positions must belong to the same faction")
        if (positions.any { it.world != this.world })
            throw IllegalArgumentException("All positions must belong to the same world")

        this.positions.addAll(positions)
        updateCluster()
    }

    private fun updateCluster() {
        calculateCenter()
        outerNodes.scheduleUpdate()
        DynmapModule.dynmapModule().dynmapModuleHandle.factionClusterChange(this)
    }
    private fun calculateCenter() {
        if (positions.isEmpty())
            return

        centerX = positions.map { it.x }.average()
        centerY = positions.map { it.y }.average()
    }

    private fun detectOuterNodes(): MutableList<WorldPosition> {
        val nodes = mutableSetOf<WorldPosition>()
        val neighbourCount = mutableMapOf<WorldPosition, Int>()

        fun incrementNeighbourCount(neighbourCount: MutableMap<WorldPosition, Int>, pos: WorldPosition) {
            neighbourCount[pos] = neighbourCount.getOrDefault(pos, 0) + 1
        }

        positions.forEach { position ->
            val blockX = position.x * 16
            val blockY = position.y * 16
            val worldPos1 = WorldPosition(position.world, blockX, blockY)
            val worldPos2 = WorldPosition(position.world, blockX + 1, blockY)
            val worldPos3 = WorldPosition(position.world, blockX, blockY + 1)
            val worldPos4 = WorldPosition(position.world, blockX + 1, blockY + 1)
            nodes.addAll(listOf(worldPos1, worldPos2, worldPos3, worldPos4))
            incrementNeighbourCount(neighbourCount, worldPos1)
            incrementNeighbourCount(neighbourCount, worldPos2)
            incrementNeighbourCount(neighbourCount, worldPos3)
            incrementNeighbourCount(neighbourCount, worldPos4)
        }

        val unsortedOuterNodes = nodes.filter { neighbourCount.getOrDefault(it, 0) < 4 }
            .toMutableSet()
        var current = unsortedOuterNodes.firstOrNull()
        val outerNodes = mutableListOf<WorldPosition>()
        while (current != null) {
            outerNodes.add(current)
            val next = unsortedOuterNodes.minByOrNull { current!!.distanceTo(it) }
            unsortedOuterNodes.remove(current)
            current = next
        }
        return outerNodes
    }
}