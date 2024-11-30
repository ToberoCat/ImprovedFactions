package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.factions.PowerAccumulationChangeReason
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.PowerType
import io.github.toberocat.improvedfactions.utils.arguments.EnumArgument
import io.github.toberocat.improvedfactions.utils.arguments.PowerArgument
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionArgument
import io.github.toberocat.toberocore.command.SubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.power.set.description",
    category = CommandCategory.ADMIN_CATEGORY,
    module = PowerRaidsModule.MODULE_NAME
)
class PowerAddCommand(
    private val plugin: ImprovedFactionsPlugin
) : SubCommand("add") {
    override fun options() = Options.getFromConfig(plugin, label) { options, _ ->
        //options.addFactionNameOption(2)
    }
    override fun arguments() = arrayOf<Argument<*>>(
        EnumArgument(PowerType::class.java, "base.command.args.power-type"),
        PowerArgument(),
        FactionArgument(),
    )

    override fun handleCommand(sender: CommandSender, args: Array<String>): Boolean {
        val faction: Faction
        val power: Int
        val powerType: PowerType
        if (sender is Player) {
            val arguments = parseArgs(sender, args)
            powerType = arguments.get<PowerType>(0) ?: return false
            power = arguments.get<Int>(1) ?: return false
            faction = arguments.get<Faction>(2) ?: return false
        } else {
            if (args.size < 3) {
                throw CommandException("base.exceptions.arg-doesnt-match", emptyMap())
            }
            powerType = PowerType.valueOf(args[0].uppercase())
            power = args[1].toIntOrNull() ?: throw CommandException("base.exceptions.arg-doesnt-match", emptyMap())
            faction = FactionHandler.getFaction(args.drop(2).joinToString(separator = " ")) ?: throw CommandException("base.exceptions.arg-doesnt-match", emptyMap())
        }

        loggedTransaction {
            when (powerType) {
                PowerType.ACCUMULATED -> faction.setAccumulatedPower(faction.accumulatedPower + power, PowerAccumulationChangeReason.OTHER)
                PowerType.MAXIMUM -> faction.setMaxPower(faction.maxPower + power)
            }
        }

        return true
    }
}