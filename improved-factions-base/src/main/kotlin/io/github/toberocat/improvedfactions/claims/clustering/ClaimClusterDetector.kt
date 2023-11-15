package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.factions.Faction

class ClaimClusterDetector(private val queryProvider: ClaimQueryProvider) {
    val clusterMap = mutableMapOf<Position, Int>()
    val clusters = mutableMapOf<Int, Cluster>()

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
            neighborClusters.size == 1 -> assignToCluster(position, neighborClusters[0])
            else -> {
                assignToCluster(position, neighborClusters[0])
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
    fun getCluster(clusterId: Int) = clusters.getOrDefault(clusterId, null)
    fun getCluster(position: Position): Cluster? = getClusterId(position)?.let {
        val cluster = getCluster(it)
        return if (cluster?.positions?.firstOrNull()?.factionId != position.factionId) null else cluster
    }

    fun removePosition(position: Position) {
        if (position !in clusterMap)
            return

        val clusterIndex = clusterMap[position]
        val cluster = clusters[clusterIndex]?.also { it.remove(position) }
            ?: throw IllegalArgumentException()
        clusterMap.remove(position)

        val unreachablePositions = ClusterReachabilityChecker(cluster.positions).getUnreachablePositions()
        if (unreachablePositions.isEmpty())
            return

        cluster.removeAll(unreachablePositions)
        if (cluster.isEmpty())
            clusters.remove(clusterIndex)

        unreachablePositions.forEach(clusterMap::remove)
        unreachablePositions.forEach { insertPosition(it) }
    }

    fun removeCluster(clusterId: Int) {
        clusters[clusterId]?.positions?.forEach(clusterMap::remove)
        clusters.remove(clusterId)
    }

    private fun getNeighboringClusters(position: Position) =
        queryProvider.querySameFactionNeighbours(position)
            .mapNotNull { clusterMap.getOrDefault(it, null) }

    private fun createNewCluster(position: Position) {
        val clusterId = clusters.size
        clusters.getOrPut(clusterId) { Cluster(position.factionId) }.add(position)
        clusterMap[position] = clusterId
    }

    private fun assignToCluster(position: Position, clusterId: Int) {
        clusters[clusterId]?.add(position)
        clusterMap[position] = clusterId
    }

    private fun mergeClusters(clusterIds: List<Int>) {
        val newClusterPositions = clusterIds.flatMap { clusters[it]?.positions ?: emptyList() }
        clusterIds.forEach { clusters.remove(it) }
        clusters[clusterIds[0]] = Cluster(newClusterPositions[0].factionId, newClusterPositions.toMutableSet())
        newClusterPositions.forEach { assignToCluster(it, clusterIds[0]) }
    }
}