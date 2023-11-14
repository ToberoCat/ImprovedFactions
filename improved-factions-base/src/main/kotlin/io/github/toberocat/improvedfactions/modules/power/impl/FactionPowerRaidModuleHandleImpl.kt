package io.github.toberocat.improvedfactions.modules.power.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.exceptions.NotEnoughPowerException
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.utils.getEnum
import io.github.toberocat.improvedfactions.utils.getUnsignedDouble
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.TimeUnit
import kotlin.math.*
import kotlin.time.times

class FactionPowerRaidModuleHandleImpl(private val module: PowerRaidsModule) : FactionPowerRaidModuleHandle {
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

    private var taskId = 0

    override fun memberJoin(faction: Faction) {
        faction.maxPower += ceil(calculatePowerChange(faction)).toInt()
    }

    override fun memberLeave(faction: Faction) {
        faction.maxPower -= floor(calculatePowerChange(faction)).toInt()
        faction.accumulatedPower = min(faction.accumulatedPower, faction.maxPower)
    }

    override fun claimChunk(faction: Faction) {
        val cost = floor(getNextClaimCost(faction)).toInt()
        if (cost > faction.accumulatedPower)
            throw NotEnoughPowerException()
        faction.accumulatedPower -= cost
    }


    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        val config = plugin.config
        Bukkit.getScheduler().cancelTask(taskId)
        baseMemberConstant = config.getUnsignedDouble("$configPath.base-member-constant", baseMemberConstant)
        accumulationTickDelay = (config.getEnum<TimeUnit>("$configPath.accumulation-rate.unit")
            ?: TimeUnit.HOURS).toSeconds(abs(config.getLong("$configPath.accumulation-rate.value", 1)))
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
        taskId = Bukkit.getScheduler()
            .runTaskTimer(plugin, ::accumulateAll, accumulationTickDelay, accumulationTickDelay).taskId
    }

    private fun accumulateAll() = transaction {
        Faction.all().forEach {
            it.accumulatedPower = min(it.maxPower.toDouble(), it.accumulatedPower + getPowerAccumulated(it)).toInt()
        }
    }

    private fun getNextClaimCost(faction: Faction) =
        baseClaimPowerCost * claimPowerCostGrowth.pow(faction.claims().count().toInt())

    private fun getPowerAccumulated(faction: Faction) =
        baseAccumulation + max(getActivePowerAccumulation(faction) - getInactivePowerAccumulation(faction), 0.0)

    private fun getActivePowerAccumulation(faction: Faction) =
        (1 + faction.countActiveMembers(accumulationTickDelay) / faction.members().count().toDouble()).pow(
            activeAccumulationExponent
        ) * accumulationMultiplier

    private fun getInactivePowerAccumulation(faction: Faction) =
        faction.countInactiveMembers(inactiveMilliseconds) * inactiveAccumulationMultiplier * accumulationMultiplier

    private fun calculatePowerChange(faction: Faction) = baseMemberConstant * 1f / faction.members().count()
}