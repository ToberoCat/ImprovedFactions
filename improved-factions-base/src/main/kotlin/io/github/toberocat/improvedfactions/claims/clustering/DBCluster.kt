package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.claims.clustering.position.WorldPosition
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.utils.DatabaseLazyUpdate
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class DBCluster(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<DBCluster>(Clusters)

    private var cachedCenterX by Clusters.centerX
    private var cachedCenterY by Clusters.centerY

    private var centerLazyUpdate by Clusters.centerLazyUpdate
    var isOuterNodeLazyUpdate by Clusters.isOuterNodeLazyUpdate

    var world by Clusters.world
    var type by Clusters.type

    var center = DatabaseLazyUpdate(
        { cachedCenterX to cachedCenterY },
        { loggedTransaction { centerLazyUpdate = it } },
        { loggedTransaction { centerLazyUpdate } },
        { calculateCenter() }
    )

    fun removeAll(claims: Set<FactionClaim>) {
        claims.forEach { it.cluster = null }
        updateCluster()
    }

    fun getClaims() = FactionClaim.find { FactionClaims.clusterId eq id }.toList()

    private fun updateCluster() {
        centerLazyUpdate = true
        isOuterNodeLazyUpdate = true
        DynmapModule.dynmapModule().dynmapModuleHandle.clusterChange(this)
    }

    private fun calculateCenter() {
        loggedTransaction {
            val claims = getClaims()
            cachedCenterX = claims.map { it.chunkX }.average()
            cachedCenterY = claims.map { it.chunkZ }.average()
        }
    }

    fun updateOuterNodes() {
        val claims = getClaims()
        detectOuterNodes(claims).forEach { outerNodes ->

        }
    }

    private fun detectOuterNodes(claims: List<FactionClaim>): List<List<WorldPosition>> {
        val positions = claims.map { it.toPosition() }.toSet()
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
                next = openSupportingPoints.filter { it.isSameAxis(next!!, allowedAxis) }
                    .minByOrNull { it.distanceTo(next!!) }
            }
            loop.add(loop.first()) // Close the polygon
            outerNodes.add(loop)
        }
        return outerNodes
    }
}