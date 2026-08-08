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

    fun process(player: Player, ban: OfflinePlayer): CommandProcessResult? {
        val user = player.cachedUser()
        if (!user.isInFaction()) {
            return notInFaction()
        }

        if (!user.hasPermission(Permissions.MANAGE_BANS)) {
            return noPermission()
        }

        val targetId = StorageManager.cache.user(ban.uniqueId)?.id ?: return banNotFound()
        val existingBan = StorageManager.cache.bans(user.factionId).firstOrNull { it.userId == targetId }
            ?: return banNotFound()
        val targetName = ban.name ?: "Unknown"
        return player.respondAfter(GameStateCommands.deleteBan(existingBan.id)) {
            unbannedTarget("targetName" to targetName)
        }
    }
}
