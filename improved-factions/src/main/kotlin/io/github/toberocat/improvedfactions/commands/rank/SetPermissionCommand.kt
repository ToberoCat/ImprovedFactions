package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "rank set",
    category = CommandCategory.PERMISSION_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("permissionUpdated"),
        CommandResponse("noPermission"),
        CommandResponse("notInFaction")
    ]
)
abstract class SetPermissionCommand : SetPermissionCommandContext() {

    fun process(player: Player, rank: RankSnapshot, permission: String, value: Boolean): CommandProcessResult? {
        val user = player.cachedUser()
        if (!user.isInFaction()) {
            return notInFaction()
        }

        if (!user.hasPermission(Permissions.MANAGE_PERMISSIONS)) {
            return noPermission()
        }

        return player.respondAfter(GameStateCommands.setPermission(rank.id, permission, value)) { permissionUpdated(
            "rank" to rank.name,
            "permission" to permission,
            "value" to value.toString()
        ) }
    }
}
