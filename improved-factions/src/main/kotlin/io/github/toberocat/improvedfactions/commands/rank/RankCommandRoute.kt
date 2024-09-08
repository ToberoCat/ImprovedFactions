package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.CommandRoute
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

const val RANK_COMMAND_DESCRIPTION = "base.command.rank.description"
const val RANK_COMMAND_CATEGORY = CommandCategory.PERMISSION_CATEGORY


@CommandMeta(
    description = RANK_COMMAND_DESCRIPTION,
    category = RANK_COMMAND_CATEGORY
)
open class RankCommandRoute(plugin: ImprovedFactionsPlugin) : CommandRoute("rank", plugin) {
    init {
        addChild(CreateRankCommand(plugin))
        addChild(AssignRankCommand(plugin))
        addChild(DeleteRankCommand(plugin))
        addChild(SetPermissionCommand(plugin))
        addChild(DefaultRankCommand(plugin))
        addChild(EditPermissionsCommand(plugin))
    }

    override fun options(): Options = super.options()
        .cmdOpt(FactionPermissionOption(Permissions.MANAGE_PERMISSIONS))
        .cmdOpt(InFactionOption(true))

    override fun handle(player: Player, args: Array<String>): Boolean {
        player.sendLocalized("base.command.rank.header")
        loggedTransaction {
            val user = player.factionUser()
            user.faction()
                ?.listRanks()
                ?.filter { user.canManage(it) }
                ?.forEach {
                    player.sendLocalized("base.command.rank.rank-overview", mapOf(
                        "name" to it.name,
                        "priority" to it.priority.toString(),
                        "countAssignedUsers" to it.countAssignedUsers().toString()
                    ))
                }
        }
        return true
    }
}