package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.factionUser
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
        val factionUser = player.factionUser()
        if (!factionUser.isInFaction()) {
            return notInFaction()
        }

        if (!factionUser.hasPermission(Permissions.VIEW_POWER)) {
            return noPermission()
        }

        val faction = factionUser.faction() ?: return notInFaction()
        return showPowerInfo(player, faction)
    }

    fun process(sender: CommandSender, faction: Faction) = showPowerInfo(sender, faction)

    private fun showPowerInfo(sender: CommandSender, faction: Faction): CommandProcessResult {
        sender.sendCommandResult(powerHeader())

        val activeAccumulation = PowerRaidsModule.powerModuleHandle.getActivePowerAccumulation(faction)
        val inactiveAccumulation = PowerRaidsModule.powerModuleHandle.getInactivePowerAccumulation(faction)
        val claimKeep = PowerRaidsModule.powerModuleHandle.getClaimMaintenanceCost(faction)
        val currentlyAccumulated = PowerRaidsModule.powerModuleHandle.getPowerAccumulated(activeAccumulation, inactiveAccumulation)
        val nextClaimCost = PowerRaidsModule.powerModuleHandle.getNextClaimCost(faction)

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