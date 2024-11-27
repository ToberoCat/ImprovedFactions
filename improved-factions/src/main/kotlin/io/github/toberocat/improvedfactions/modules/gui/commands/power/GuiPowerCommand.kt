package io.github.toberocat.improvedfactions.modules.gui.commands.power

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.power.commands.POWER_COMMAND_CATEGORY
import io.github.toberocat.improvedfactions.modules.power.commands.POWER_COMMAND_DESCRIPTION
import io.github.toberocat.improvedfactions.modules.power.commands.POWER_COMMAND_MODULE
import io.github.toberocat.improvedfactions.modules.power.commands.PowerCommand
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.entity.Player

@CommandMeta(
    description = POWER_COMMAND_DESCRIPTION,
    category = POWER_COMMAND_CATEGORY,
    module = POWER_COMMAND_MODULE
)
class GuiPowerCommand(
    private val guiEngineApi: GuiEngineApi,
    plugin: ImprovedFactionsPlugin,
    powerHandle: FactionPowerRaidModuleHandleImpl
) :
    PowerCommand(plugin, powerHandle) {
    override fun handle(player: Player, args: Array<out String>): Boolean {
        loggedTransaction {
            val faction = player.factionUser().faction() ?: throw CommandException(
                "power.command.power.faction-needed", emptyMap()
            )
            val activeAccumulation = powerHandle.getActivePowerAccumulation(faction)
            val inactiveAccumulation = powerHandle.getInactivePowerAccumulation(faction)
            val claimKeep = powerHandle.getClaimMaintenanceCost(faction)
            guiEngineApi.openGui(
                player, "power/overview", mapOf(
                    "power" to faction.accumulatedPower.toString(),
                    "maxPower" to faction.maxPower.toString(),
                    "currently-accumulated" to stringify(
                        powerHandle.getPowerAccumulated(
                            activeAccumulation,
                            inactiveAccumulation
                        )
                    ),
                    "active-accumulation" to stringify(activeAccumulation),
                    "inactive-accumulation" to stringify(inactiveAccumulation),
                    "claim-keep" to stringify(claimKeep),
                    "next-claim-cost" to powerHandle.getNextClaimCost(faction).toString()
                )
            )
        }
        return true
    }
}