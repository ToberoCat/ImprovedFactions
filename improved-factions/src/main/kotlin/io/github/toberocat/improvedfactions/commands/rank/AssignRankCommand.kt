package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.user.factionUser
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

    fun process(player: Player, target: OfflinePlayer, rank: FactionRank): CommandProcessResult {
        if (!player.factionUser().isInFaction())
            return notInFaction()

        if (!player.factionUser().hasPermission(Permissions.MANAGE_PERMISSIONS))
            return noPermission()

        val targetUser = target.factionUser()
        if (targetUser.faction() != player.factionUser().faction())
            return notInSameFaction()

        targetUser.assignedRank = rank.id.value

        return rankAssigned(
            "playerName" to (target.name ?: "Unknown"),
            "rankName" to rank.name
        )
    }
}