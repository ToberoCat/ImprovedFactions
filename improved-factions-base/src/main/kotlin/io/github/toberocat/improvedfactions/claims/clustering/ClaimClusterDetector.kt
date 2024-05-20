package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.factions.Faction
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
        queryProvider.all().forEach(::insertPosition)
    }

    fun insertPosition(position: Position) {
        if (position in clusterMap)
            throw IllegalArgumentException("Position already exists in the cluster map")

        val neighborClusters = getNeighboringClusters(position)
        when {
            neighborClusters.isEmpty() -> createNewCluster(position)
            neighborClusters.size == 1 -> assignToCluster(setOf(position), neighborClusters[0])
            else -> {
                assignToCluster(setOf(position), neighborClusters[0])
                mergeClusters(neighborClusters)
            }
        }
    }

    fun markFactionClusterForUpdate(faction: Faction) = clusters.values
        .filter { it.factionId == faction.id.value }
        .forEach(Cluster::scheduleUpdate)

    fun removeFactionClusters(faction: Faction) = clusters
        .filter { it.value.factionId == faction.id.value }
        .forEach { removeCluster(it.key) }

    fun getClusterId(position: Position) = clusterMap[position]

    private fun getCluster(clusterId: UUID) = clusters.getOrDefault(clusterId, null)
    fun getCluster(position: Position): Cluster? = getClusterId(position)?.let {
        val cluster = getCluster(it)
        return if (cluster?.getReadOnlyPositions()?.firstOrNull()?.factionId != position.factionId) null else cluster
    }

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
            clusters.remove(clusterIndex)

        unreachablePositions.forEach(clusterMap::remove)
        unreachablePositions.forEach { insertPosition(it) }
    }

    fun removeCluster(clusterId: UUID) {
        clusters[clusterId]?.getReadOnlyPositions()?.forEach(clusterMap::remove)
        clusters.remove(clusterId)
    }

    private fun getNeighboringClusters(position: Position) =
        queryProvider.querySameFactionNeighbours(position)
            .mapNotNull { clusterMap.getOrDefault(it, null) }

    private fun createNewCluster(position: Position) {
        val clusterId = generateClusterId()
        val positions = mutableSetOf(position)

        clusterMap[position] = clusterId
        when (clusterId) {
            in clusters -> clusters[clusterId]?.addAll(positions)
            else -> clusters[clusterId] = Cluster(positions)
        }
    }

    private fun assignToCluster(positions: Set<Position>, clusterId: UUID) {
        clusters[clusterId]?.addAll(positions)
        positions.forEach { clusterMap[it] = clusterId }
    }

    private fun mergeClusters(clusterIds: List<UUID>) {
        val newClusterPositions = clusterIds.flatMap { clusters[it]?.getReadOnlyPositions() ?: emptyList() }
        clusterIds.forEach { clusters.remove(it) }
        clusters[clusterIds[0]] = Cluster(newClusterPositions.toMutableSet())
        assignToCluster(newClusterPositions.toSet(), clusterIds[0])
    }
}