package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.math.round

@GeneratedCommandMeta(
    label = "power",
    category = CommandCategory.POWER_CATEGORY,
    module = PowerRaidsModule.MODULE_NAME,
    responses = [
        CommandResponse("powerHeader"),
        CommandResponse("powerDetail"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class PowerCommand : PowerCommandContext() {

    fun process(player: Player): CommandProcessResult {
        val factionUser = player.cachedUser()
        if (!factionUser.isInFaction()) {
            return notInFaction()
        }

        if (!factionUser.hasPermission(Permissions.VIEW_POWER)) {
            return noPermission()
        }

        val faction = factionUser.faction() ?: return notInFaction()
        return showPowerInfo(player, faction)
    }

    fun process(sender: CommandSender, faction: FactionSnapshot) = showPowerInfo(sender, faction)

    private fun showPowerInfo(sender: CommandSender, faction: FactionSnapshot): CommandProcessResult {
        sender.sendCommandResult(powerHeader())

        val handle = PowerRaidsModule.powerModuleHandle as? FactionPowerRaidModuleHandleImpl
        val activeAccumulation = handle?.getActivePowerAccumulation(faction) ?: 0.0
        val inactiveAccumulation = handle?.getInactivePowerAccumulation(faction) ?: 0.0
        val claimKeep = handle?.getClaimMaintenanceCost(faction) ?: 0.0
        val currentlyAccumulated = handle?.getPowerAccumulated(activeAccumulation, inactiveAccumulation) ?: 0.0
        val nextClaimCost = handle?.getNextClaimCost(faction) ?: 0

        sender.sendCommandResult(details("Power", stringify(faction.accumulatedPower.toDouble())))
        sender.sendCommandResult(details("Max Power", stringify(faction.maxPower.toDouble())))
        sender.sendCommandResult(details("Active Accumulation", stringify(activeAccumulation)))
        sender.sendCommandResult(details("Inactive Accumulation", stringify(inactiveAccumulation)))
        sender.sendCommandResult(details("Claim Keep", stringify(claimKeep)))
        sender.sendCommandResult(details("Current Accumulation", stringify(currentlyAccumulated)))

        return details("Next Claim Cost", stringify(nextClaimCost.toDouble()))
    }

    private fun details(key: String, value: String, cmd: String = "") = powerDetail(
        "cmd" to cmd,
        "key" to key,
        "value" to value
    )

    private fun stringify(value: Double) = (round(value * 100) / 100).toString()
}
