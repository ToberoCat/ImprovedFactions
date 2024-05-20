package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import java.util.UUID

class ClaimClusterDetector(
    private val queryProvider: ClaimQueryProvider,
    private val generateClusterId: () -> UUID = UUID::randomUUID
) {
    val clusterMap = mutableMapOf<Position, UUID>()
    val clusters = mutableMapOf<UUID, Cluster>()

    fun detectClusters() {
        clusterMap.clear()
        clusters.clear()
        queryProvider.allFactionPositions()
            .forEach { (position, factionId) -> insertFactionPosition(position, factionId) }
        queryProvider.allZonePositions().forEach { (position, zoneType) -> insertZonePosition(position, zoneType) }
    }

    fun insertFactionPosition(position: Position, factionId: Int) {
        insertPosition(position, neighboursProvider = {
            getMatchingClusters(position) { id ->
                val factionCluster = clusters[id] as? FactionCluster ?: return@getMatchingClusters false
                return@getMatchingClusters factionCluster.factionId == factionId
            }
        }) { id, positions -> FactionCluster(factionId, id, positions) }
    }

    fun insertZonePosition(position: Position, zoneType: String) {
        insertPosition(position, neighboursProvider = {
            getMatchingClusters(position) { id ->
                val zoneCluster = clusters[id] as? ZoneCluster ?: return@getMatchingClusters false
                return@getMatchingClusters zoneCluster.zoneType == zoneType
            }
        }) { id, positions -> ZoneCluster(zoneType, id, positions) }
    }

    fun markFactionClusterForUpdate(faction: Faction) = clusters.values
        .mapNotNull { it as? FactionCluster }
        .filter { it.factionId == faction.id.value }
        .forEach(Cluster::scheduleUpdate)

    fun removeFactionClusters(faction: Faction) = clusters
        .map { it.key to it.value as? FactionCluster }
        .filter { it.second?.factionId == faction.id.value }
        .forEach { removeCluster(it.first) }

    fun getClusterId(position: Position) = clusterMap[position]

    fun getCluster(position: Position) = getClusterId(position)?.let { getCluster(it) }

    fun removePosition(position: Position) {
        if (position !in clusterMap)
            return

        val clusterIndex = clusterMap[position]
        val cluster = clusters[clusterIndex]?.also { it.removeAll(setOf(position)) }
            ?: throw IllegalArgumentException()
        clusterMap.remove(position)

        val unreachablePositions = ClusterReachabilityChecker(cluster.getReadOnlyPositions()).getUnreachablePositions()
        if (unreachablePositions.isEmpty())
            return

        cluster.removeAll(unreachablePositions)
        if (cluster.isEmpty())
            clusterIndex?.let { rawRemoveCluster(it) }

        unreachablePositions.forEach(clusterMap::remove)

        when (cluster) {
            is FactionCluster -> unreachablePositions.forEach { insertFactionPosition(it, cluster.factionId) }
            is ZoneCluster -> unreachablePositions.forEach { insertZonePosition(it, cluster.zoneType) }
            else -> throw IllegalArgumentException("Unknown cluster type")
        }
    }

    fun removeCluster(clusterId: UUID) {
        clusters[clusterId]?.getReadOnlyPositions()?.forEach(clusterMap::remove)
        rawRemoveCluster(clusterId)
    }

    private fun getCluster(clusterId: UUID) = clusters.getOrDefault(clusterId, null)

    private fun getMatchingClusters(position: Position, matchCondition: (uuid: UUID) -> Boolean) =
        queryProvider.queryNeighbours(position)
            .mapNotNull { clusterMap.getOrDefault(it, null) }
            .filter { matchCondition(it) }

    private fun createNewCluster(position: Position, clusterGenerator: (UUID, MutableSet<Position>) -> Cluster) {
        val clusterId = generateClusterId()
        val positions = mutableSetOf(position)

        clusterMap[position] = clusterId
        when (clusterId) {
            in clusters -> clusters[clusterId]?.addAll(positions)
            else -> clusters[clusterId] = clusterGenerator(clusterId, positions)
        }
    }

    private fun assignToCluster(positions: Set<Position>, clusterId: UUID) {
        clusters[clusterId]?.addAll(positions)
        positions.forEach { clusterMap[it] = clusterId }
    }

    private fun mergeClusters(
        clusterIds: List<UUID>,
        clusterGenerator: (UUID, MutableSet<Position>) -> Cluster
    ) {
        val newClusterPositions = clusterIds.flatMap { clusters[it]?.getReadOnlyPositions() ?: emptyList() }
        clusterIds.forEach { rawRemoveCluster(it) }
        val newId = clusterIds[0]
        clusters[newId] = clusterGenerator(newId, newClusterPositions.toMutableSet())
        assignToCluster(newClusterPositions.toSet(), newId)
    }

    private fun insertPosition(
        position: Position,
        neighboursProvider: () -> List<UUID>,
        createNewCluster: (UUID, MutableSet<Position>) -> Cluster
    ) {
        if (position in clusterMap)
            throw IllegalArgumentException("Position already exists in the cluster map")

        val neighborClusters = neighboursProvider()
        when {
            neighborClusters.isEmpty() -> createNewCluster(position, createNewCluster)
            neighborClusters.size == 1 -> assignToCluster(setOf(position), neighborClusters[0])
            else -> {
                assignToCluster(setOf(position), neighborClusters[0])
                mergeClusters(neighborClusters, createNewCluster)
            }
        }
    }

    private fun rawRemoveCluster(clusterId: UUID) {
        clusters.remove(clusterId)?.let(DynmapModule.dynmapModule().dynmapModuleHandle::clusterRemove)
    }
}