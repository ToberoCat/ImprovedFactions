package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule.Companion.powerRaidModule
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.utils.LazyUpdate
import java.util.UUID

class Cluster(val id: UUID, private val positions: MutableSet<Position>) {
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
        val nodes = generateNodesFromPositions()
        val hull = calculateConvexHull(nodes)
        return adjustNodesOnHull(hull)
    }

    private fun generateNodesFromPositions(): List<WorldPosition> {
        return positions.flatMap { position ->
            val blockX = position.x * 16
            val blockY = position.y * 16
            listOf(
                WorldPosition(position.world, blockX, blockY),
                WorldPosition(position.world, blockX + 16, blockY),
                WorldPosition(position.world, blockX, blockY + 16),
                WorldPosition(position.world, blockX + 16, blockY + 16)
            )
        }
    }

    private fun calculateConvexHull(nodes: List<WorldPosition>): List<WorldPosition> {
        val sortedByYX = nodes.sortedWith(compareBy<WorldPosition> { it.y }.thenBy { it.x })
        val pivot = sortedByYX.first()

        val sortedByAngle = sortedByYX.sortedBy { pivot.angleWithPivot(it) }

        val hull = mutableListOf<WorldPosition>()

        for (point in sortedByAngle) {
            while (hull.size >= 2 && !isCounterClockwise(hull[hull.size - 2], hull.last(), point)) {
                hull.removeAt(hull.size - 1)
            }
            hull.add(point)
        }

        return hull
    }

    private fun isCounterClockwise(p1: WorldPosition, p2: WorldPosition, p3: WorldPosition): Boolean {
        val crossProduct = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)
        return crossProduct > 0
    }

    private fun adjustNodesOnHull(hull: List<WorldPosition>): MutableList<WorldPosition> {
        val adjustedNodes = mutableListOf<WorldPosition>()

        for (i in hull.indices) {
            val p1 = hull[i]
            adjustedNodes.add(p1)
            adjustNode(hull, p1)?.let { adjustedNodes.add(it) }
        }

        return adjustedNodes
    }

    private fun adjustNode(hull: List<WorldPosition>, p1: WorldPosition): WorldPosition? {
        val p2 = hull[(hull.indexOf(p1) + 1) % hull.size]
        return when {
            p1.x != p2.x && p1.y != p2.y -> when {
                p1.x < p2.x -> when {
                    p1.y < p2.y -> WorldPosition(p1.world, p1.x, p2.y)
                    else -> WorldPosition(p1.world, p2.x, p1.y)
                }
                else -> when {
                    p1.y < p2.y -> WorldPosition(p1.world, p2.x, p1.y)
                    else -> WorldPosition(p1.world, p1.x, p2.y)
                }
            }
            else -> null
        }
    }
}