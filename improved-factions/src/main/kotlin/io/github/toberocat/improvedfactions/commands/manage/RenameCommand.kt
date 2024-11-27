package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.FactionNameInputArgument
import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.*
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player


@CommandMeta(
    description = "base.command.rename.description",
    category = CommandCategory.MANAGE_CATEGORY
)
class RenameCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("rename") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.cmdOpt(InFactionOption(true)).cmdOpt(IsFactionOwnerOption()).addFactionNameOption(0).cmdOpt(ArgLengthOption(1))
            .cmdOpt(FactionExistOption(0, false))
            .cmdOpt(FactionPermissionOption(Permissions.RENAME_FACTION))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        FactionNameInputArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        loggedTransaction {
            val faction = player.factionUser().faction() ?: throw CommandException(
                "base.command.rename.faction-needed", emptyMap()
            )
            faction.name = args[0]
        }
        player.sendLocalized("base.command.rename.renamed")
        return true
    }
}