package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "kick",
    category = CommandCategory.MEMBER_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("kickedPlayer"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission"),
        CommandResponse("invalidMember")
    ]
)
abstract class KickCommand : KickCommandContext() {

    fun process(player: Player, target: FactionUser?): CommandProcessResult {
        if (target == null) {
            return invalidMember()
        }

        val faction = player.factionUser().faction() ?: return notInFaction()
        if (!player.factionUser().hasPermission(Permissions.KICK_PLAYER)) {
            return noPermission()
        }

        faction.kick(target.uniqueId)
        return kickedPlayer("playerName" to (target.offlinePlayer().name ?: "Unknown"))
    }
}
