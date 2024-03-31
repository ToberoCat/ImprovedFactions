package io.github.toberocat.improvedfactions.modules.power.config

import io.github.toberocat.improvedfactions.utils.getEnum
import io.github.toberocat.improvedfactions.utils.getUnsignedDouble
import org.bukkit.configuration.file.FileConfiguration
import java.util.concurrent.TimeUnit
import kotlin.math.abs

data class PowerManagementConfig(
    var baseMemberConstant: Double = 50.0,
    var accumulationTickDelay: Long = 1,
    var inactiveMilliseconds: Long = 1,
    var baseAccumulation: Int = 0,
    var activeAccumulationExponent: Double = 2.0,
    var inactiveAccumulationMultiplier: Double = 2.0,
    var accumulationMultiplier: Double = 10.0,
    var baseClaimPowerCost: Double = 5.0,
    var claimPowerCostGrowth: Double = 1.1,
    var claimPowerKeep: Double = 1.0,
    var playerDeathCost: Int = 5,
    var siegeBreachProgress: Double = 1.0,
    var siegeResistanceProgress: Double = .5,
    var siegeClaimRecoverySpeed: Double = 1.0
) {
    private val configPath = "factions.power-management"
    fun reload(config: FileConfiguration) {
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
        siegeBreachProgress = config.getUnsignedDouble("$configPath.siege.breach-progress", siegeBreachProgress)
        siegeResistanceProgress = config.getUnsignedDouble("$configPath.siege.resistance-progress", siegeResistanceProgress)
        siegeClaimRecoverySpeed = config.getUnsignedDouble("$configPath.siege.claim-recovery-speed", siegeClaimRecoverySpeed)
    }
}
