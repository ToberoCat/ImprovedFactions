package io.github.toberocat.improvedfactions.commands.admin

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.managers.ByPassManager
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "admin bypass",
    category = CommandCategory.ADMIN_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("bypassAdded"),
        CommandResponse("bypassRemoved"),
        CommandResponse("playerNotFound")
    ]
)
abstract class ByPassCommand : ByPassCommandContext() {

    fun processPlayer(executor: Player, target: Player?): CommandProcessResult {
        val actualTarget = target ?: executor
        return bypass(actualTarget)
    }

    fun process(sender: CommandSender, target: Player) = bypass(target)

    private fun bypass(player: Player): CommandProcessResult {
        val targetId = player.uniqueId
        return if (ByPassManager.isBypassing(targetId)) {
            ByPassManager.removeBypass(targetId)
            bypassRemoved("player" to player.name)
        } else {
            ByPassManager.addBypass(targetId)
            bypassAdded("player" to player.name)
        }
    }
}