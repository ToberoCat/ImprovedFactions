package io.github.toberocat.improvedfactions.claims.clustering.detector

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.clustering.ClusterType
import io.github.toberocat.improvedfactions.claims.clustering.cluster.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.cluster.FactionCluster
import io.github.toberocat.improvedfactions.claims.clustering.cluster.ZoneCluster
import io.github.toberocat.improvedfactions.claims.clustering.query.ClaimQueryProvider
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.concurrent.thread

class ClaimClusterDetector(
    private val queryProvider: ClaimQueryProvider,
    private val generateClusterId: () -> UUID = UUID::randomUUID
) {
    val clusters: List<Cluster>
        get() = transaction { Cluster.all().toList() }

    fun detectClusters() {
        ImprovedFactionsPlugin.instance.logger.info("[detector] Detecting clusters...")

        val job = thread {
            queryProvider.allFactionPositions()
                .forEach { (position, factionId) -> insertFactionPosition(position, factionId) }
            ImprovedFactionsPlugin.instance.logger.info("[detector] Finished loading faction positions.")
        }

        queryProvider.allZonePositions()
            .forEach { (position, zoneType) -> insertZonePosition(position, zoneType) }
        ImprovedFactionsPlugin.instance.logger.info("[detector] Finished loading zone positions.")
        job.join()
        ImprovedFactionsPlugin.instance.logger.info("[detector] All done!")
    }

    fun insertFactionPosition(claim: FactionClaim, factionId: Int) {
        loggedTransaction {
            insertPosition(claim, neighboursProvider = {
                getMatchingClusters(claim) { id ->
                    val cluster = Cluster.findById(id)?.findAdditionalType() as? FactionCluster
                        ?: return@getMatchingClusters false
                    return@getMatchingClusters cluster.faction.id.value == factionId
                }
            }) { id, claims ->
                val factionAdditional = FactionCluster.new {
                    this.faction =
                        FactionHandler.getFaction(factionId) ?: throw IllegalArgumentException("Faction not found")
                }
                Cluster.new(id) {
                    type = ClusterType.FACTION
                    typeReferenceId = factionAdditional.id.value
                    world = claims.first().world
                }.addAll(claims)
            }
        }
    }

    fun insertZonePosition(claim: FactionClaim, zoneType: String) {
        loggedTransaction {
            insertPosition(claim, neighboursProvider = {
                getMatchingClusters(claim) { id ->
                    val cluster = Cluster.findById(id)?.findAdditionalType() as? ZoneCluster
                        ?: return@getMatchingClusters false
                    return@getMatchingClusters cluster.zoneType == zoneType
                }
            }) { id, claims ->
                val zone = zoneType
                val zoneAdditional = ZoneCluster.new {
                    this.zoneType = zone
                }
                Cluster.new(id) {
                    type = ClusterType.ZONE
                    typeReferenceId = zoneAdditional.id.value
                    world = claims.first().world
                }.addAll(claims)
            }
        }
    }

    fun markFactionClusterForUpdate(faction: Faction) {
        loggedTransaction {
            faction.claims()
                .mapNotNull { it.claimCluster }
                .distinctBy { it.id.value }
                .forEach { it.updateCluster() }
        }
    }

    fun removeFactionClusters(faction: Faction) {
        loggedTransaction {
            faction.claims()
                .mapNotNull { it.claimCluster }
                .distinctBy { it.id.value }
                .forEach { it.delete() }
        }
    }

    fun getClusterId(claim: FactionClaim) = loggedTransaction { claim.claimCluster?.id?.value }

    fun getCluster(claim: FactionClaim) = loggedTransaction { claim.claimCluster }

    fun removePosition(claim: FactionClaim) {
        loggedTransaction {
            val cluster = claim.claimCluster ?: return@loggedTransaction
            cluster.removeAll(setOf(claim))
            DynmapModule.dynmapModule().dynmapModuleHandle.removeClaim(claim)

            val unreachablePositions = ClusterReachabilityChecker(cluster.getClaims().map { it.toPosition() }.toSet())
                .getUnreachablePositions()
                .mapNotNull { it.getFactionClaim() }
                .toSet()
            if (unreachablePositions.isEmpty())
                return@loggedTransaction

            cluster.removeAll(unreachablePositions)
            when (cluster.type) {
                ClusterType.FACTION -> {
                    val factionId = (cluster.findAdditionalType() as? FactionCluster)?.faction?.id?.value
                        ?: throw IllegalArgumentException("Faction claim not actually a faction claim")
                    unreachablePositions.forEach { insertFactionPosition(it, factionId) }
                }

                ClusterType.ZONE -> {
                    val zoneType = (cluster.findAdditionalType() as? ZoneCluster)?.zoneType
                        ?: throw IllegalArgumentException("Faction claim not actually a zone type claim")
                    unreachablePositions.forEach { insertZonePosition(it, zoneType) }
                }
            }
        }
    }

    fun deleteCluster(clusterId: UUID) {
        loggedTransaction {
            Cluster.findById(clusterId)?.delete()
        }
    }

    private fun getCluster(clusterId: UUID) = Cluster.findById(clusterId)

    private fun getMatchingClusters(position: FactionClaim, matchCondition: (uuid: UUID) -> Boolean) =
        queryProvider.queryNeighbours(position)
            .mapNotNull { it.claimCluster?.id?.value }
            .filter { matchCondition(it) }


    private fun assignToCluster(claims: Set<FactionClaim>, clusterId: UUID) {
        Cluster.findById(clusterId)?.addAll(claims)
    }

    private fun mergeClusters(
        clusterIds: List<UUID>,
        clusterGenerator: (UUID, Set<FactionClaim>) -> Unit
    ) {
        val newClusterPositions = clusterIds.flatMap { Cluster.findById(it)?.getClaims() ?: emptyList() }
        clusterIds.forEach { deleteCluster(it) }
        val newId = clusterIds[0]
        clusterGenerator(newId, newClusterPositions.toSet())
        assignToCluster(newClusterPositions.toSet(), newId)
    }

    private fun insertPosition(
        claim: FactionClaim,
        neighboursProvider: () -> List<UUID>,
        generator: (UUID, Set<FactionClaim>) -> Unit
    ) {
        if (claim.claimCluster != null)
            throw IllegalArgumentException("Position already exists in the cluster map")

        val neighborClusters = neighboursProvider()
        when {
            neighborClusters.isEmpty() -> generator(generateClusterId(), setOf(claim))
            neighborClusters.size == 1 -> assignToCluster(setOf(claim), neighborClusters[0])
            else -> {
                assignToCluster(setOf(claim), neighborClusters[0])
                mergeClusters(neighborClusters, generator)
            }
        }
    }

    fun getAffectedClaims() = transaction { Cluster.all().sumOf { it.getClaims().size } }
}