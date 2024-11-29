package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ConfirmOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.leave.description",
    category = CommandCategory.MANAGE_CATEGORY
)
class LeaveCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("leave") {
    override fun options(): Options = Options.getFromConfig(plugin, "leave") { options, _ ->
        options.cmdOpt(ConfirmOption()).cmdOpt(InFactionOption(true))
    }

    override fun arguments(): Array<Argument<*>> = emptyArray()

    override fun handle(player: Player, args: Array<out String>): Boolean {
        loggedTransaction {
            val faction = player.factionUser().faction()
                ?: throw CommandException("base.command.leave.faction-deleted", emptyMap())
            faction.leave(player.uniqueId)
        }
        player.sendLocalized("base.command.leave.left")
        return true
    }
}