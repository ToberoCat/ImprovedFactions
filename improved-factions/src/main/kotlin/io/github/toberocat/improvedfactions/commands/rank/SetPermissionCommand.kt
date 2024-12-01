package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.FactionPermission
import io.github.toberocat.improvedfactions.permissions.FactionPermissions
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

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

    fun process(player: Player, rank: FactionRank, permission: String, value: Boolean): CommandProcessResult {
        if (!player.factionUser().isInFaction()) {
            return notInFaction()
        }

        if (player.factionUser().hasPermission(Permissions.MANAGE_PERMISSIONS)) {
            return noPermission()
        }

        val factionPermission = FactionPermission.find(
            FactionPermissions.rankId eq rank.id.value and (FactionPermissions.permission eq permission)
        ).firstOrNull() ?: FactionPermission.new {
            rankId = rank.id.value
            this.permission = permission
        }
        factionPermission.allowed = value

        return permissionUpdated(
            "rank" to rank.name,
            "permission" to permission,
            "value" to value.toString()
        )
    }
}