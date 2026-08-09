package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.OfflinePlayer
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

    fun process(player: Player, target: OfflinePlayer?): CommandProcessResult? {
        if (target == null) {
            return invalidMember()
        }

        val user = player.cachedUser()
        val faction = user.faction() ?: return notInFaction()
        if (!user.hasPermission(Permissions.KICK_PLAYER)) {
            return noPermission()
        }

        if (target.cachedUser().factionId != faction.id) return invalidMember()
        val targetName = target.name ?: "Unknown"
        return player.respondAfter(GameStateCommands.setUserFaction(
            target.uniqueId, noFactionId, 0, PowerRaidsModule.config.baseMemberConstant
        )) {
            kickedPlayer("playerName" to targetName)
        }
    }
}
