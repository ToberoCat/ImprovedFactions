package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.ban.FactionBan
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionBannedArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.PlayerNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

@CommandMeta(
    description = "base.command.unban.description",
    category = CommandCategory.MEMBER_CATEGORY
)
class UnBanCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("unban") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options
            .cmdOpt(InFactionOption(true))
            .cmdOpt(PlayerNameOption(0))
            .cmdOpt(FactionPermissionOption(Permissions.MANAGE_BANS))
            .cmdOpt(ArgLengthOption(1))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        FactionBannedArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val ban = parseArgs(player, args).get<FactionBan>(0) ?: return false
        transaction { ban.delete() }
        player.sendLocalized("base.command.unban.unbanned-target")
        return true
    }
}