package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.factions.FactionJoinType
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "joinMode",
    category = CommandCategory.MANAGE_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("joinModeChanged"),
        CommandResponse("invalidJoinType"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission"),
    ]
)
abstract class JoinTypeCommand : JoinTypeCommandContext() {
    fun process(player: Player, joinType: FactionJoinType): CommandProcessResult {
        return setJoinType(player, joinType)
    }

    fun process(sender: CommandSender, target: OfflinePlayer, joinType: FactionJoinType): CommandProcessResult {
        return setJoinType(target, joinType)
    }

    private fun setJoinType(player: OfflinePlayer, joinType: FactionJoinType): CommandProcessResult {
        val factionUser = player.factionUser()
        val faction = factionUser.faction()
            ?: return notInFaction()

        if (!factionUser.hasPermission(Permissions.SET_JOIN_TYPE)) {
            return noPermission()
        }

        faction.factionJoinType = joinType
        return joinModeChanged("mode" to joinType.name.lowercase())
    }
}