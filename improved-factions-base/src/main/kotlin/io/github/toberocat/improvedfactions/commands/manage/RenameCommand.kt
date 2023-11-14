package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.FactionNameInputArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.*
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction


@CommandMeta(
    description = "base.command.rename.description",
    category = CommandCategory.MANAGE_CATEGORY
)
class RenameCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("rename") {
    override fun options(): Options = Options.getFromConfig(plugin, "rename") { options, _ ->
        options.opt(InFactionOption(true)).opt(IsFactionOwnerOption()).addFactionNameOption(0).opt(ArgLengthOption(1))
            .opt(FactionExistOption(0, false))
            .cmdOpt(FactionPermissionOption(Permissions.RENAME_FACTION))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        FactionNameInputArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        transaction {
            val faction = player.factionUser().faction() ?: throw CommandException(
                "base.command.rename.faction-needed", emptyMap()
            )
            faction.broadcast(
                "base.faction.renamed", mapOf(
                    "old" to faction.name, "new" to args[0]
                )
            )
            faction.name = args[0]
        }
        player.sendLocalized("base.command.rename.renamed")
        return true
    }
}