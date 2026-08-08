package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "rank assign",
    category = CommandCategory.PERMISSION_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("rankAssigned"),
        CommandResponse("notInFaction"),
        CommandResponse("notInSameFaction"),
        CommandResponse("noPermission"),
    ]
)
abstract class AssignRankCommand : AssignRankCommandContext() {

    fun process(player: Player, target: OfflinePlayer, rank: RankSnapshot): CommandProcessResult? {
        val user = player.cachedUser()
        if (!user.isInFaction())
            return notInFaction()

        if (!user.hasPermission(Permissions.MANAGE_PERMISSIONS))
            return noPermission()

        val targetUser = target.cachedUser()
        if (targetUser.factionId != user.factionId)
            return notInSameFaction()

        val targetName = target.name ?: "Unknown"
        return player.respondAfter(GameStateCommands.assignRank(target.uniqueId, rank.id)) { rankAssigned(
            "playerName" to targetName,
            "rankName" to rank.name
        ) }
    }
}
