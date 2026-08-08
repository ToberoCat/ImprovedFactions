package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "rank edit",
    category = CommandCategory.PERMISSION_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("editPermissionsHeader"),
        CommandResponse("permissionDetails"),
        CommandResponse("invalidRank"),
        CommandResponse("notInFaction"),
        CommandResponse("rankEdited")
    ]
)
abstract class EditPermissionsCommand : EditPermissionsCommandContext() {

    fun process(player: Player, rank: RankSnapshot): CommandProcessResult {
        val userFaction = player.cachedUser().faction()
            ?: return notInFaction()

        if (userFaction.id != rank.factionId) {
            return invalidRank()
        }

        player.sendCommandResult(editPermissionsHeader("rankName" to rank.name))

        val permissionDetails = Permissions.knownPermissions.keys.map {
            permissionDetails(
                "rankName" to rank.name,
                "permission" to it,
                "value" to (it in rank.permissions).toString()
            )
        }

        permissionDetails.forEach { player.sendCommandResult(it) }
        return rankEdited()
    }
}
