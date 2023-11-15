package io.github.toberocat.improvedfactions.modules.power.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.exceptions.NotEnoughPowerException
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.PowerAccumulationChangeReason
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.getEnum
import io.github.toberocat.improvedfactions.utils.getUnsignedDouble
import io.github.toberocat.toberocore.util.MathUtils
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.TimeUnit
import kotlin.math.*

class FactionPowerRaidModuleHandleImpl(private val module: PowerRaidsModule) : FactionPowerRaidModuleHandle, Listener {
    private val configPath = "factions.power-management";
    private var baseMemberConstant: Double = 50.0
    private var accumulationTickDelay: Long = 1
    private var inactiveMilliseconds: Long = 1
    private var baseAccumulation: Int = 0
    private var activeAccumulationExponent: Double = 2.0
    private var inactiveAccumulationMultiplier: Double = 2.0
    private var accumulationMultiplier: Double = 10.0
    private var baseClaimPowerCost: Double = 5.0
    private var claimPowerCostGrowth: Double = 1.1
    private var claimPowerKeep: Double = 1.0
    private var playerDeathCost: Int = 5

    private var accumulateTaskId = 0
    private var claimKeepCostTaskId = 1

    override fun memberJoin(faction: Faction) {
        faction.setMaxPower(faction.maxPower + ceil(calculatePowerChange(faction.members().count())).toInt())
    }

    override fun memberLeave(faction: Faction) {
        faction.setMaxPower(faction.maxPower - floor(calculatePowerChange(faction.members().count() + 1)).toInt())
    }

    override fun claimChunk(faction: Faction) {
        val cost = getNextClaimCost(faction)
        if (cost > faction.accumulatedPower)
            throw NotEnoughPowerException()
        faction.setAccumulatedPower(faction.accumulatedPower - cost, PowerAccumulationChangeReason.CHUNK_CLAIMED)
    }

    override fun calculateUnprotectedChunks(cluster: Cluster, unprotectedPositions: MutableSet<Position>) {
        val faction = Faction.findById(cluster.factionId)
            ?: throw IllegalArgumentException("Faction is missing")

        val totalClaims = faction.claims().count()
        val claimMaintenanceCost = getClaimMaintenanceCost(totalClaims)

        val clusterClaimsRatio = cluster.positions.size.toDouble() / totalClaims
        val clusterPowerCost = claimMaintenanceCost * clusterClaimsRatio

        val positionSrqDistances = cluster.positions.map { it.distanceSquaredTo(cluster.centerX, cluster.centerY) }

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

        val positions = cluster.positions.toList()
        unprotectedPositions.addAll(distancePercentages
            .mapIndexedNotNull { index, element -> if (element * claimPowerCost >= threshold) positions[index] else null })
    }

    @EventHandler
    private fun onDeath(event: PlayerDeathEvent) {
        transaction {
            val faction = event.entity.factionUser().faction() ?: return@transaction
            faction.setAccumulatedPower(faction.accumulatedPower - playerDeathCost, PowerAccumulationChangeReason.PLAYER_DEATH)
        }
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        val config = plugin.config
        Bukkit.getScheduler().cancelTask(accumulateTaskId)
        Bukkit.getScheduler().cancelTask(claimKeepCostTaskId)

        baseMemberConstant = config.getUnsignedDouble("$configPath.base-member-constant", baseMemberConstant)
        accumulationTickDelay = (config.getEnum<TimeUnit>("$configPath.accumulation-rate.unit")
            ?: TimeUnit.HOURS).toSeconds(abs(config.getLong("$configPath.accumulation-rate.value", 1))) * 20
        baseAccumulation = config.getInt("$configPath.base-accumulation", baseAccumulation)
        activeAccumulationExponent =
            config.getUnsignedDouble("$configPath.accumulation-active-exponent", activeAccumulationExponent)
        inactiveAccumulationMultiplier =
            config.getUnsignedDouble("$configPath.accumulation-inactive-multiplier", inactiveAccumulationMultiplier)
        accumulationMultiplier = config.getUnsignedDouble("$configPath.accumulation-multiplier", accumulationMultiplier)
        inactiveMilliseconds = (config.getEnum<TimeUnit>("$configPath.inactive.unit") ?: TimeUnit.DAYS).toSeconds(
            abs(
                config.getLong("$configPath.inactive.value", 1)
            )
        )
        baseClaimPowerCost = config.getUnsignedDouble("$configPath.base-claim-power-cost", baseClaimPowerCost)
        claimPowerCostGrowth = config.getUnsignedDouble("$configPath.claim-power-cost-growth", claimPowerCostGrowth)
        claimPowerKeep = config.getUnsignedDouble("$configPath.claim-power-keep", claimPowerKeep)
        playerDeathCost = abs(config.getInt("$configPath.player-death-cost", playerDeathCost))
        accumulateTaskId = Bukkit.getScheduler()
            .runTaskTimer(plugin, ::accumulateAll, accumulationTickDelay, accumulationTickDelay).taskId
        claimKeepCostTaskId = Bukkit.getScheduler()
            .runTaskTimer(
                plugin,
                ::claimKeepCostsCollector,
                accumulationTickDelay + accumulationTickDelay / 2,
                accumulationTickDelay
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
        floor(baseClaimPowerCost * claimPowerCostGrowth.pow(faction.claims().count().toInt())).toInt()

    fun getPowerAccumulated(activeAccumulation: Double, negativAccumulation: Double) =
        baseAccumulation + max(activeAccumulation - negativAccumulation, 0.0)

    private fun getPowerAccumulated(faction: Faction) =
        getPowerAccumulated(getActivePowerAccumulation(faction), getInactivePowerAccumulation(faction))

    fun getActivePowerAccumulation(faction: Faction) =
        (1 + faction.countActiveMembers(accumulationTickDelay) / faction.members().count().toDouble()).pow(
            activeAccumulationExponent
        ) * accumulationMultiplier


    fun getClaimMaintenanceCost(faction: Faction) = getClaimMaintenanceCost(faction.claims().count())
    fun getInactivePowerAccumulation(faction: Faction) =
        faction.countInactiveMembers(inactiveMilliseconds) * inactiveAccumulationMultiplier * accumulationMultiplier

    private fun calculatePowerChange(members: Long) = baseMemberConstant * (1f / members)
    private fun getClaimMaintenanceCost(claims: Long) = claims * claimPowerKeep
}