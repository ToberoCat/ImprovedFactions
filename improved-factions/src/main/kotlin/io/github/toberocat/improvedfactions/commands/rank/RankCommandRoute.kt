package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "rank",
    category = CommandCategory.PERMISSION_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("rankHeader"),
        CommandResponse("rankOverview"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission"),
        CommandResponse("ranksListed")
    ]
)
abstract class RankCommandRoute : RankCommandRouteContext() {

    fun process(player: Player): CommandProcessResult {
        val user = player.factionUser()
        val faction = user.faction() ?: return notInFaction()

        if (!user.hasPermission(Permissions.MANAGE_PERMISSIONS)) {
            return noPermission()
        }

        player.sendCommandResult(rankHeader())

        val ranks = loggedTransaction {
            faction.listRanks()
                .filter { user.canManage(it) }
                .map {
                    rankOverview(
                        "name" to it.name,
                        "priority" to it.priority.toString(),
                        "countAssignedUsers" to it.countAssignedUsers().toString()
                    )
                }
        }

        ranks.forEach { player.sendCommandResult(it) }
        return ranksListed()
    }
}