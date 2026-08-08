package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
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
        val user = player.cachedUser()
        val faction = user.faction() ?: return notInFaction()

        if (!user.hasPermission(Permissions.MANAGE_PERMISSIONS)) {
            return noPermission()
        }

        player.sendCommandResult(rankHeader())

        val ranks = StorageManager.cache.ranks(faction.id)
                .filter { user.canManage(it) }
                .map {
                    rankOverview(
                        "name" to it.name,
                        "priority" to it.priority.toString(),
                        "countAssignedUsers" to StorageManager.cache.snapshot()?.users.orEmpty().values.count { user -> user.rankId == it.id }.toString()
                    )
                }

        ranks.forEach { player.sendCommandResult(it) }
        return ranksListed()
    }
}
