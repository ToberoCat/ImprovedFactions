package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.ban.FactionBan
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "unban",
    category = CommandCategory.MEMBER_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("unbannedTarget"),
        CommandResponse("banNotFound"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class UnBanCommand : UnBanCommandContext() {

    fun process(player: Player, ban: FactionBan): CommandProcessResult {
        if (!player.factionUser().isInFaction()) {
            return notInFaction()
        }

        if (!player.factionUser().hasPermission(Permissions.MANAGE_BANS)) {
            return noPermission()
        }

        val existingBan = FactionBan.findById(ban.id) ?: return banNotFound()
        existingBan.delete()

        return unbannedTarget("targetName" to (existingBan.user.offlinePlayer().name ?: "Unknown"))
    }
}
