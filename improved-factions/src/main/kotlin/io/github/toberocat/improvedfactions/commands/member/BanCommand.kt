package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.factionUser
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

    fun process(player: Player, target: OfflinePlayer): CommandProcessResult {
        if (target == player) {
            return cantBanSelf()
        }

        val faction = player.factionUser().faction() ?: return notInFaction()

        if (!player.factionUser().hasPermission(Permissions.MANAGE_BANS)) {
            return noPermission()
        }

        faction.ban(target.factionUser())
        return bannedTarget("target" to (target.name ?: "Unknown"))
    }
}