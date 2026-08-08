package io.github.toberocat.improvedfactions.commands.member

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
    label = "ban",
    category = CommandCategory.MEMBER_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("bannedTarget"),
        CommandResponse("cantBanSelf"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class BanCommand : BanCommandContext() {

    fun process(player: Player, target: OfflinePlayer): CommandProcessResult? {
        if (target == player) {
            return cantBanSelf()
        }

        val user = player.cachedUser()
        val faction = user.faction() ?: return notInFaction()

        if (!user.hasPermission(Permissions.MANAGE_BANS)) {
            return noPermission()
        }

        if (target.cachedUser().factionId != faction.id) {
            return notInFaction()
        }

        val targetName = target.name ?: "Unknown"
        return player.respondAfter(GameStateCommands.banUser(faction.id, target.uniqueId)) {
            bannedTarget("target" to targetName)
        }
    }
}
