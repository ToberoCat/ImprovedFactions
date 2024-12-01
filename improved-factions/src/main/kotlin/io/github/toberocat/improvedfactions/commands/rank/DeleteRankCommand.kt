package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.FactionUsers
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "rank delete",
    category = CommandCategory.PERMISSION_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("noPermission"),
        CommandResponse("rankDeleted"),
        CommandResponse("rankIsDefault"),
        CommandResponse("notInFaction"),
        CommandResponse("invalidRank"),
    ]
)
abstract class DeleteRankCommand : DeleteRankCommandContext() {

    fun process(player: Player, rank: FactionRank, fallbackRank: FactionRank?): CommandProcessResult {
        val user = player.factionUser()
        if (!user.hasPermission(Permissions.MANAGE_PERMISSIONS)) {
            return noPermission()
        }

        val faction = user.faction() ?: return notInFaction()

        if (faction.defaultRank == rank.id.value) {
            return rankIsDefault("rankName" to rank.name)
        }

        val fallBackOrDefault = fallbackRank ?: faction.getDefaultRank()
        FactionUser.find { FactionUsers.assignedRank eq rank.id.value }
            .forEach { it.assignedRank = fallBackOrDefault.id.value }

        rank.delete()

        return rankDeleted(
            "rankName" to rank.name,
            "fallbackRankName" to fallBackOrDefault.name
        )
    }
}