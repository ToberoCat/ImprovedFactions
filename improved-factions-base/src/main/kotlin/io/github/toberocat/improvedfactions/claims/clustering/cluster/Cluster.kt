package io.github.toberocat.improvedfactions.claims.clustering.cluster

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.claims.clustering.ClusterType
import io.github.toberocat.improvedfactions.claims.clustering.position.WorldPosition
import io.github.toberocat.improvedfactions.database.DatabaseManager
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.utils.DatabaseLazyUpdate
import io.github.toberocat.improvedfactions.utils.LazyUpdate
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class Cluster(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Cluster>(Clusters)

    private var cachedCenterX by Clusters.centerX
    private var cachedCenterY by Clusters.centerY
    var typeReferenceId by Clusters.typeReferenceId

    private var centerLazyUpdate by Clusters.centerLazyUpdate
    private val outerNodes = LazyUpdate { detectOuterNodes(getClaims()) }

    var world by Clusters.world
    var type by Clusters.type

    var center = DatabaseLazyUpdate(
        { cachedCenterX to cachedCenterY },
        { DatabaseManager.loggedTransaction { centerLazyUpdate = it } },
        { DatabaseManager.loggedTransaction { centerLazyUpdate } },
        { calculateCenter() }
    )

    override fun delete() {
        getClaims().forEach { it.claimCluster = null }
        findAdditionalType()?.delete()
        DynmapModule.dynmapModule().dynmapModuleHandle.clusterRemove(this)
        super.delete()
    }

    fun getOuterNodes() = outerNodes.get()

    fun removeAll(claims: Set<FactionClaim>) {
        claims.forEach { it.claimCluster = null }
        updateCluster()
        if (isEmpty()) {
            delete()
        }
    }

    fun addAll(claims: Set<FactionClaim>) {
        if (claims.any { it.world != this.world })
            throw IllegalArgumentException("All claims must belong to the same world")

        claims.forEach { it.claimCluster = this }
        updateCluster()
    }

    fun getClaims() =  FactionClaim.find { FactionClaims.clusterId eq id }.toList()

    fun isEmpty() = FactionClaim.count(FactionClaims.clusterId eq id) == 0L

    fun getColor() = findAdditionalType()?.getColor() ?: 0xffffff

    fun updateCluster() {
        center.scheduleUpdate()
        outerNodes.scheduleUpdate()
        findAdditionalType()?.scheduleUpdate()
        DynmapModule.dynmapModule().dynmapModuleHandle.clusterChange(this)
    }

    fun findAdditionalType(): AdditionalClusterType? = when (type) {
        ClusterType.FACTION -> FactionCluster.findById(typeReferenceId)
        ClusterType.ZONE -> ZoneCluster.findById(typeReferenceId)
    }

    private fun calculateCenter() {
        DatabaseManager.loggedTransaction {
            val claims = getClaims()
            cachedCenterX = claims.map { it.chunkX }.average()
            cachedCenterY = claims.map { it.chunkZ }.average()
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