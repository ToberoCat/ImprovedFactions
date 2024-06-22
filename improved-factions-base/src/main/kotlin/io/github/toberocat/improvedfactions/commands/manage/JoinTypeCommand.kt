package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.FactionJoinType
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.EnumArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.joinMode.description",
    category = CommandCategory.MANAGE_CATEGORY
)
class JoinTypeCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("joinMode") {
    override fun options() = Options.getFromConfig(plugin, label) { options, _ ->
        options
            .cmdOpt(InFactionOption(true))
            .cmdOpt(FactionPermissionOption(Permissions.SET_JOIN_TYPE))
    }

    override fun arguments() = arrayOf(
        EnumArgument(FactionJoinType::class.java, "base.command.args.joinMode")
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val joinType = parseArgs(player, args).get<FactionJoinType>(0) ?: return false
        loggedTransaction {
            val faction = player.factionUser().faction() ?: return@loggedTransaction false
            faction.factionJoinType = joinType
        }

        player.sendLocalized("base.command.joinMode.changed")
        return true
    }
}