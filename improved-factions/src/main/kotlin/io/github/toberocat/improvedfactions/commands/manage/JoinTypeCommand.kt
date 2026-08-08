package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.factions.FactionJoinType
import io.github.toberocat.improvedfactions.permissions.Permissions
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
    fun process(player: Player, joinType: FactionJoinType): CommandProcessResult? {
        return setJoinType(player, player, joinType)
    }

    fun process(sender: CommandSender, target: OfflinePlayer, joinType: FactionJoinType): CommandProcessResult? {
        return setJoinType(sender, target, joinType)
    }

    private fun setJoinType(sender: CommandSender, player: OfflinePlayer, joinType: FactionJoinType): CommandProcessResult? {
        val factionUser = player.cachedUser()
        val faction = factionUser.faction()
            ?: return notInFaction()

        if (!factionUser.hasPermission(Permissions.SET_JOIN_TYPE)) {
            return noPermission()
        }

        return sender.respondAfter(GameStateCommands.setJoinType(faction.id, joinType)) {
            joinModeChanged("mode" to joinType.name.lowercase())
        }
    }
}
