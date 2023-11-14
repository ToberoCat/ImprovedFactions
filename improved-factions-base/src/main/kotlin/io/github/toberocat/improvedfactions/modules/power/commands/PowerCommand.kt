package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
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
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.round

@CommandMeta(
    description = "base.command.info.description",
    category = CommandCategory.POWER_CATEGORY
)
class PowerCommand(private val plugin: ImprovedFactionsPlugin,
                   private val powerHandle: FactionPowerRaidModuleHandleImpl) : PlayerSubCommand("power") {
    override fun options() = Options.getFromConfig(plugin, label) { options, _ ->
        options
            .cmdOpt(InFactionOption(true))
            .cmdOpt(FactionPermissionOption(Permissions.VIEW_POWER))
    }

    override fun arguments() = arrayOf<Argument<*>>()

    override fun handle(player: Player, args: Array<out String>): Boolean {
        transaction {
            val faction = player.factionUser().faction() ?: throw CommandException(
                "power.command.power.faction-needed", emptyMap()
            )
            val activeAccumulation = powerHandle.getActivePowerAccumulation(faction)
            val inactiveAccumulation = powerHandle.getInactivePowerAccumulation(faction)
            val claimKeep = powerHandle.getClaimMaintenanceCost(faction)
            val negativAccumulation = powerHandle.getNegativPowerAccumulation(inactiveAccumulation, claimKeep)
            plugin.guiEngineApi.openGui(player, "power/overview", mapOf(
                "power" to faction.accumulatedPower.toString(),
                "maxPower" to faction.maxPower.toString(),
                "currently-accumulated" to stringify(powerHandle.getPowerAccumulated(activeAccumulation, negativAccumulation)),
                "active-accumulation" to stringify(activeAccumulation),
                "inactive-accumulation" to stringify(inactiveAccumulation),
                "claim-keep" to stringify(claimKeep),
                "negativ-accumulation" to stringify(negativAccumulation),
                "next-claim-cost" to powerHandle.getNextClaimCost(faction).toString()
            ))
        }
        return true
    }

    private fun stringify(value: Double) = (round(value * 100) / 100).toString()
}