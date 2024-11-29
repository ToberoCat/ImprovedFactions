package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionMemberArgument
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.PlayerNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.kick.description",
    category = CommandCategory.MEMBER_CATEGORY
)
class KickCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("kick") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.cmdOpt(PlayerNameOption(0))
            .cmdOpt(InFactionOption(true))
            .cmdOpt(FactionPermissionOption(Permissions.KICK_PLAYER))
            .cmdOpt(ArgLengthOption(1))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        FactionMemberArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val target = parseArgs(player, args).get<FactionUser>(0) ?: return false
        loggedTransaction {
            target.faction()?.kick(target.uniqueId)
        }
        player.sendLocalized("base.command.kick.kicked")
        return true
    }
}