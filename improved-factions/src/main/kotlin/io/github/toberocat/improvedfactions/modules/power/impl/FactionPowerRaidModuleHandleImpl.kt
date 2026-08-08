package io.github.toberocat.improvedfactions.modules.power.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.power.config.PowerManagementConfig
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import org.bukkit.Bukkit
import kotlin.math.*

const val TICKS_TO_MS = 50

class FactionPowerRaidModuleHandleImpl(private val config: PowerManagementConfig) : FactionPowerRaidModuleHandle {

    private var accumulateTaskId: Int = 0
    private var claimKeepCostTaskId: Int = 1
    private var lastAccumulationMs = System.currentTimeMillis()
    private var lastClaimKeepCostMs = System.currentTimeMillis()

    fun nextAccumulationCycleTime() = lastAccumulationMs + config.accumulationTickDelay * TICKS_TO_MS
    fun nextClaimKeepCostCycleTime() = lastClaimKeepCostMs + config.accumulationTickDelay * TICKS_TO_MS

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        Bukkit.getScheduler().cancelTask(accumulateTaskId)
        Bukkit.getScheduler().cancelTask(claimKeepCostTaskId)

        accumulateTaskId = Bukkit.getScheduler()
            .runTaskTimer(plugin, ::accumulateAll, config.accumulationTickDelay, config.accumulationTickDelay).taskId
        claimKeepCostTaskId = Bukkit.getScheduler().runTaskTimer(
                plugin,
                ::claimKeepCostsCollector,
                config.accumulationTickDelay + config.accumulationTickDelay / 2,
                config.accumulationTickDelay
            ).taskId
    }

    private fun claimKeepCostsCollector() {
        lastClaimKeepCostMs = System.currentTimeMillis()
        val updates = StorageManager.cache.factions().associate { faction ->
            faction.id to (faction.accumulatedPower - getClaimMaintenanceCost(faction).toInt())
                .coerceIn(-faction.maxPower, faction.maxPower)
        }
        if (updates.isNotEmpty()) GameStateCommands.setAccumulatedPower(updates)
    }

    private fun accumulateAll() {
        lastAccumulationMs = System.currentTimeMillis()
        val updates = StorageManager.cache.factions().associate { faction ->
            faction.id to (faction.accumulatedPower + getPowerAccumulated(faction).toInt())
                .coerceIn(-faction.maxPower, faction.maxPower)
        }
        if (updates.isNotEmpty()) GameStateCommands.setAccumulatedPower(updates)
    }

    override fun getPowerAccumulated(activeAccumulation: Double, inactiveAccumulation: Double) =
        config.baseAccumulation + max(activeAccumulation - inactiveAccumulation, 0.0)

    private fun getClaimMaintenanceCost(claims: Long) = claims * config.claimPowerKeep

    fun playerDie(faction: FactionSnapshot) {
        GameStateCommands.setPower(
            faction.id,
            accumulated = (faction.accumulatedPower - config.playerDeathCost)
                .coerceIn(-faction.maxPower, faction.maxPower)
        )
    }

    fun getNextClaimCost(faction: FactionSnapshot) =
        floor(config.baseClaimPowerCost * config.claimPowerCostGrowth.pow(faction.claimCount)).toInt()

    fun getClaimMaintenanceCost(faction: FactionSnapshot) = faction.claimCount * config.claimPowerKeep

    fun getActivePowerAccumulation(faction: FactionSnapshot): Double {
        val members = StorageManager.cache.factionMembers(faction.id)
        if (members.isEmpty()) return 0.0
        val minStamp = System.currentTimeMillis() - config.accumulationTickDelay / 20 * 1000
        val active = members.count { id -> Bukkit.getOfflinePlayer(id).let { it.isOnline || it.lastPlayed > minStamp } }
        return (1 + active / members.size.toDouble()).pow(config.activeAccumulationExponent) * config.accumulationMultiplier
    }

    fun getInactivePowerAccumulation(faction: FactionSnapshot): Double {
        val minStamp = System.currentTimeMillis() - config.inactiveMilliseconds
        val inactive = StorageManager.cache.factionMembers(faction.id).count { id ->
            Bukkit.getOfflinePlayer(id).let { !it.isOnline && it.lastPlayed <= minStamp }
        }
        return inactive * config.inactiveAccumulationMultiplier * config.accumulationMultiplier
    }

    fun getPowerAccumulated(faction: FactionSnapshot) =
        getPowerAccumulated(getActivePowerAccumulation(faction), getInactivePowerAccumulation(faction))
}
