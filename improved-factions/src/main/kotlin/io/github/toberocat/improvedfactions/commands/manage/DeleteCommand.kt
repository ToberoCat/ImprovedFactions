package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.IsFactionOwnerOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ConfirmOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

/**
 * Created: 05.08.2023
 * @author Tobias Madlberger (Tobias)
 */

@CommandMeta(
    description = "base.command.delete.description",
    category = CommandCategory.MANAGE_CATEGORY
)
class DeleteCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("delete") {
    override fun options(): Options = Options.getFromConfig(plugin, "delete") { options, _ ->
        options.cmdOpt(ConfirmOption()).cmdOpt(InFactionOption(true)).cmdOpt(IsFactionOwnerOption())
    }

    override fun arguments(): Array<Argument<*>> = emptyArray()

    override fun handle(player: Player, args: Array<out String>): Boolean {
        loggedTransaction {
            player.factionUser().faction()?.delete()
        }

        player.sendLocalized("base.command.delete.deleted")
        return true
    }
}