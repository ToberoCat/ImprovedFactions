package io.github.toberocat.improvedfactions.integrations.papi

import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.utils.toCountdownTime
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.pow

object PlaceholderIntegration {
    fun parsePlaceholder(player: OfflinePlayer?, params: String): String? {
        // PlaceholderAPI requests can originate from async consumers. Never touch
        // OfflinePlayer/Bukkit state from such a caller and never block it waiting
        // for the server thread.
        if (!Bukkit.isPrimaryThread()) return BaseModule.config.defaultPlaceholders[params]
        if (player == null) return BaseModule.config.defaultPlaceholders[params]
        val cache = StorageManager.cache
        val user = cache.user(player.uniqueId) ?: return BaseModule.config.defaultPlaceholders[params]
        val faction = cache.faction(user.factionId)
        return when (params) {
            "owner" -> faction?.let { cache.playerName(it.owner) }
            "name" -> faction?.name
            "rank" -> user.rankName
            "join_mode" -> faction?.joinType
            "members_total" -> faction?.let { cache.factionMembers(it.id).size.toString() }
            "members_online" -> faction?.let {
                cache.factionMembers(it.id).count { memberId -> Bukkit.getPlayer(memberId) != null }.toString()
            }
            "power" -> faction?.accumulatedPower?.toString()
            "max_power" -> faction?.maxPower?.toString()
            "next_power_gain" -> faction?.let(::nextPowerGain)?.let { String.format("%.2f", it) }
            "active_accumulation" -> faction?.let(::activeAccumulation)?.toString()
            "inactive_accumulation" -> faction?.let(::inactiveAccumulation)?.toString()
            "claim_upkeep_cost" -> faction?.let {
                (it.claimCount * PowerRaidsModule.config.claimPowerKeep).toString()
            }
            "next_claim_cost" -> faction?.let {
                floor(
                    PowerRaidsModule.config.baseClaimPowerCost *
                        PowerRaidsModule.config.claimPowerCostGrowth.pow(it.claimCount)
                ).toInt().toString()
            }
            "next_accumulation_cycle" -> powerHandle()?.let {
                (it.nextAccumulationCycleTime() - System.currentTimeMillis()).toCountdownTime()
            }
            "next_claim_keep_cost_cycle" -> powerHandle()?.let {
                (it.nextClaimKeepCostCycleTime() - System.currentTimeMillis()).toCountdownTime()
            }
            else -> BaseModule.config.defaultPlaceholders[params]
        }
    }

    private fun nextPowerGain(faction: FactionSnapshot): Double = PowerRaidsModule.config.baseAccumulation +
        max(activeAccumulation(faction) - inactiveAccumulation(faction), 0.0)

    private fun activeAccumulation(faction: FactionSnapshot): Double {
        val members = StorageManager.cache.factionMembers(faction.id)
        if (members.isEmpty()) return 0.0
        val minStamp = System.currentTimeMillis() - PowerRaidsModule.config.accumulationTickDelay / 20 * 1000
        val active = members.count { playerId ->
            Bukkit.getOfflinePlayer(playerId).let { it.isOnline || it.lastPlayed > minStamp }
        }
        return (1 + active / members.size.toDouble()).pow(PowerRaidsModule.config.activeAccumulationExponent) *
            PowerRaidsModule.config.accumulationMultiplier
    }

    private fun inactiveAccumulation(faction: FactionSnapshot): Double {
        val minStamp = System.currentTimeMillis() - PowerRaidsModule.config.inactiveMilliseconds
        val inactive = StorageManager.cache.factionMembers(faction.id).count { playerId ->
            Bukkit.getOfflinePlayer(playerId).let { !it.isOnline && it.lastPlayed <= minStamp }
        }
        return inactive * PowerRaidsModule.config.inactiveAccumulationMultiplier *
            PowerRaidsModule.config.accumulationMultiplier
    }

    private fun powerHandle() = PowerRaidsModule.powerModuleHandle as? FactionPowerRaidModuleHandleImpl
}
