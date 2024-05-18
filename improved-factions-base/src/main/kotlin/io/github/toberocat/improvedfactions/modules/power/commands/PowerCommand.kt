package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import kotlin.math.round

const val POWER_COMMAND_DESCRIPTION = "base.command.power.description"
const val POWER_COMMAND_CATEGORY = CommandCategory.POWER_CATEGORY
const val POWER_COMMAND_MODULE = PowerRaidsModule.MODULE_NAME

@CommandMeta(
    description = POWER_COMMAND_DESCRIPTION,
    category = POWER_COMMAND_CATEGORY,
    module = POWER_COMMAND_MODULE
)
open class PowerCommand(
    private val plugin: ImprovedFactionsPlugin,
    protected val powerHandle: FactionPowerRaidModuleHandleImpl
) : PlayerSubCommand("power") {

    init {
        addChild(PowerSetCommand(plugin))
        addChild(PowerAddCommand(plugin))
    }

    override fun options() = Options.getFromConfig(plugin, label) { options, _ ->
        options
            .cmdOpt(InFactionOption(true))
            .cmdOpt(FactionPermissionOption(Permissions.VIEW_POWER))
    }

    override fun arguments() = arrayOf<Argument<*>>()

    override fun handle(player: Player, args: Array<out String>): Boolean {
        player.sendLocalized("base.command.power.header")

        loggedTransaction {
            val faction = player.factionUser().faction() ?: throw CommandException(
                "power.command.power.faction-needed", emptyMap()
            )
            val activeAccumulation = powerHandle.getActivePowerAccumulation(faction)
            val inactiveAccumulation = powerHandle.getInactivePowerAccumulation(faction)
            val claimKeep = powerHandle.getClaimMaintenanceCost(faction)
            val currentlyAccumulated = powerHandle.getPowerAccumulated(
                activeAccumulation,
                inactiveAccumulation
            )
            val nextClaimCost = powerHandle.getNextClaimCost(faction)


            player.showDetails("Active Accumulation", stringify(activeAccumulation))
            player.showDetails("Inactive Accumulation", stringify(inactiveAccumulation))
            player.showDetails("Claim Keep", stringify(claimKeep))
            player.showDetails("Current Accumulation", stringify(currentlyAccumulated))
            player.showDetails("Next Claim Cost", stringify(nextClaimCost.toDouble()))
        }
        return true
    }

    private fun Player.showDetails(key: String, value: String, cmd: String = "") {
        sendLocalized(
            "base.command.power.detail", mapOf(
                "cmd" to cmd,
                "key" to key,
                "value" to value
            )
        )
    }

    protected fun stringify(value: Double) = (round(value * 100) / 100).toString()
}