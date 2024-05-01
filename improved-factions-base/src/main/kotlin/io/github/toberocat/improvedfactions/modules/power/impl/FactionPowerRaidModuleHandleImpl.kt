package io.github.toberocat.improvedfactions.modules.power.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.exceptions.NotEnoughPowerException
import io.github.toberocat.improvedfactions.exceptions.NotEnoughPowerForClaimException
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.PowerAccumulationChangeReason
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.config.PowerManagementConfig
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.getEnum
import io.github.toberocat.improvedfactions.utils.getUnsignedDouble
import io.github.toberocat.toberocore.util.MathUtils
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.TimeUnit
import kotlin.math.*

class FactionPowerRaidModuleHandleImpl(private val config: PowerManagementConfig) : FactionPowerRaidModuleHandle, Listener {

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

    override fun calculateUnprotectedChunks(cluster: Cluster, unprotectedPositions: MutableSet<Position>) {
        val faction = Faction.findById(cluster.factionId)
            ?: throw IllegalArgumentException("Faction is missing")

        val totalClaims = faction.claims().count()
        val claimMaintenanceCost = getClaimMaintenanceCost(totalClaims)

        val clusterClaimsRatio = cluster.getReadOnlyPositions().size.toDouble() / totalClaims
        val clusterPowerCost = claimMaintenanceCost * clusterClaimsRatio

        val positionSrqDistances = cluster.getReadOnlyPositions().map { it.distanceSquaredTo(cluster.centerX, cluster.centerY) }

        val biggestDistance = positionSrqDistances.maxOrNull() ?: return
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

        val positions = cluster.getReadOnlyPositions().toList()
        unprotectedPositions.addAll(distancePercentages
            .mapIndexedNotNull { index, element -> if (element * claimPowerCost >= threshold) positions[index] else null })
    }

    @EventHandler
    private fun onDeath(event: PlayerDeathEvent) {
        transaction {
            val faction = event.entity.factionUser().faction() ?: return@transaction
            faction.setAccumulatedPower(faction.accumulatedPower - config.playerDeathCost, PowerAccumulationChangeReason.PLAYER_DEATH)
        }
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

    private fun claimKeepCostsCollector() = transaction {
        Faction.all().forEach {
            it.setAccumulatedPower(
                it.accumulatedPower - getClaimMaintenanceCost(it).toInt(),
                PowerAccumulationChangeReason.CHUNK_KEEP_COST
            )
        }
    }

    private fun accumulateAll() = transaction {
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
}