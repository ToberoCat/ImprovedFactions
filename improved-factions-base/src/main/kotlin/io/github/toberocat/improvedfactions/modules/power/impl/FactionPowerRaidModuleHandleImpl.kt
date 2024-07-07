package io.github.toberocat.improvedfactions.modules.power.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.clustering.cluster.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.cluster.FactionCluster
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.exceptions.NotEnoughPowerForClaimException
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.PowerAccumulationChangeReason
import io.github.toberocat.improvedfactions.modules.power.config.PowerManagementConfig
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import io.github.toberocat.toberocore.util.MathUtils
import org.bukkit.Bukkit
import org.bukkit.Chunk
import kotlin.math.*

class FactionPowerRaidModuleHandleImpl(private val config: PowerManagementConfig) : FactionPowerRaidModuleHandle {

    private var accumulateTaskId: Int = 0
    private var claimKeepCostTaskId: Int = 1

    override fun memberJoin(faction: Faction) {
        faction.setMaxPower(faction.maxPower + ceil(calculatePowerChange(faction.members().count())).toInt())
    }

    override fun memberLeave(faction: Faction) {
        faction.setMaxPower(faction.maxPower - floor(calculatePowerChange(faction.members().count() + 1)).toInt())
    }

    override fun claimChunk(chunk: Chunk, faction: Faction) {
        val cost = getNextClaimCost(faction)
        if (cost > faction.accumulatedPower)
            throw NotEnoughPowerForClaimException(chunk)
        faction.setAccumulatedPower(faction.accumulatedPower - cost, PowerAccumulationChangeReason.CHUNK_CLAIMED)
    }

    override fun calculateUnprotectedChunks(cluster: Cluster): Set<FactionClaim> {
        if (!config.allowOverclaim) return emptySet()
        val faction = (cluster.findAdditionalType() as? FactionCluster)?.faction
            ?: throw IllegalArgumentException("No faction cluster")
        val totalClaims = faction.claims().count()
        val claimMaintenanceCost = getClaimMaintenanceCost(totalClaims)

        val clusterClaimsRatio = cluster.getClaims().size.toDouble() / totalClaims
        val clusterPowerCost = claimMaintenanceCost * clusterClaimsRatio

        val (centerX, centerY) = cluster.center.get()
        val positionSrqDistances = cluster.getClaims().map { it.toPosition().distanceSquaredTo(centerX, centerY) }

        val biggestDistance = positionSrqDistances.maxOrNull() ?: return emptySet()
        val distancePercentages = positionSrqDistances.map { it / biggestDistance }
        val totalDistancePercentageSum = distancePercentages.sum()

        val claimPowerCost = clusterPowerCost / totalDistancePercentageSum
        val threshold = sqrt(
            (faction.maxPower + MathUtils.clamp(
                faction.accumulatedPower - claimMaintenanceCost,
                -faction.maxPower.toDouble(),
                faction.maxPower.toDouble()
            )) * clusterClaimsRatio
        )

        val positions = cluster.getClaims()
        return distancePercentages
            .mapIndexedNotNull { index, element -> if (element * claimPowerCost >= threshold) positions[index] else null }
            .toSet()
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        Bukkit.getScheduler().cancelTask(accumulateTaskId)
        Bukkit.getScheduler().cancelTask(claimKeepCostTaskId)

        accumulateTaskId = Bukkit.getScheduler()
            .runTaskTimer(plugin, ::accumulateAll, config.accumulationTickDelay, config.accumulationTickDelay).taskId
        claimKeepCostTaskId = Bukkit.getScheduler()
            .runTaskTimer(
                plugin,
                ::claimKeepCostsCollector,
                config.accumulationTickDelay + config.accumulationTickDelay / 2,
                config.accumulationTickDelay
            ).taskId
    }

    private fun claimKeepCostsCollector() = loggedTransaction {
        Faction.all().forEach {
            it.setAccumulatedPower(
                it.accumulatedPower - getClaimMaintenanceCost(it).toInt(),
                PowerAccumulationChangeReason.CHUNK_KEEP_COST
            )
        }
    }

    private fun accumulateAll() = loggedTransaction {
        Faction.all().forEach {
            it.setAccumulatedPower(
                it.accumulatedPower + getPowerAccumulated(it).toInt(),
                PowerAccumulationChangeReason.PASSIV_ENERGY_ACCUMULATION
            )
        }
    }

    fun getNextClaimCost(faction: Faction) =
        floor(config.baseClaimPowerCost * config.claimPowerCostGrowth.pow(faction.claims().count().toInt())).toInt()

    fun getPowerAccumulated(activeAccumulation: Double, negativAccumulation: Double) =
        config.baseAccumulation + max(activeAccumulation - negativAccumulation, 0.0)

    private fun getPowerAccumulated(faction: Faction) =
        getPowerAccumulated(getActivePowerAccumulation(faction), getInactivePowerAccumulation(faction))

    fun getActivePowerAccumulation(faction: Faction) =
        (1 + faction.countActiveMembers(config.accumulationTickDelay) / faction.members().count().toDouble()).pow(
            config.activeAccumulationExponent
        ) * config.accumulationMultiplier


    fun getClaimMaintenanceCost(faction: Faction) = getClaimMaintenanceCost(faction.claims().count())
    fun getInactivePowerAccumulation(faction: Faction) =
        faction.countInactiveMembers(config.inactiveMilliseconds) * config.inactiveAccumulationMultiplier * config.accumulationMultiplier

    private fun calculatePowerChange(members: Long) = config.baseMemberConstant * (1f / members)
    private fun getClaimMaintenanceCost(claims: Long) = claims * config.claimPowerKeep
    fun playerDie(faction: Faction) {
        faction.setAccumulatedPower(
            faction.accumulatedPower - config.playerDeathCost,
            PowerAccumulationChangeReason.PLAYER_DEATH
        )
    }
}