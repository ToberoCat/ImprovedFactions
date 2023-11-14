package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
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

@CommandMeta(
    description = "base.command.info.description",
    category = CommandCategory.POWER_CATEGORY
)
class PowerCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("power") {
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
            player.sendLocalized(
                "power.command.power.success", mapOf(
                    "power" to faction.accumulatedPower.toString(),
                    "maxPower" to faction.maxPower.toString()
                )
            )
        }
        return true
    }
}