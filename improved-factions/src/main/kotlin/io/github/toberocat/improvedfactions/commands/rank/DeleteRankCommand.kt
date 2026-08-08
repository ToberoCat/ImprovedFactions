package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
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

    fun process(player: Player, rank: RankSnapshot, fallbackRank: RankSnapshot?): CommandProcessResult? {
        val user = player.cachedUser()
        if (!user.hasPermission(Permissions.MANAGE_PERMISSIONS)) {
            return noPermission()
        }

        val faction = user.faction() ?: return notInFaction()

        if (faction.defaultRankId == rank.id) {
            return rankIsDefault("rankName" to rank.name)
        }

        val fallBackOrDefault = fallbackRank ?: StorageManager.cache.rank(faction.defaultRankId)
            ?: return invalidRank()
        return player.respondAfter(GameStateCommands.deleteRank(rank.id, fallBackOrDefault.id)) { rankDeleted(
            "rankName" to rank.name,
            "fallbackRankName" to fallBackOrDefault.name
        ) }
    }
}
