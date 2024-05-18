package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.PlayerArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.PlayerNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.ban.description",
    category = CommandCategory.MEMBER_CATEGORY
)
class BanCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("ban") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options
            .cmdOpt(InFactionOption(true))
            .cmdOpt(PlayerNameOption(0))
            .cmdOpt(FactionPermissionOption(Permissions.MANAGE_BANS))
            .cmdOpt(ArgLengthOption(1))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        PlayerArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val target = parseArgs(player, args).get<Player>(0) ?: return false
        if (target == player)
            throw CommandException("base.command.ban.cant-ban-yourself", emptyMap())
        loggedTransaction {
            player.factionUser().faction()?.ban(target.factionUser())
        }

        player.sendLocalized("base.command.ban.banned-target")
        return true
    }
}