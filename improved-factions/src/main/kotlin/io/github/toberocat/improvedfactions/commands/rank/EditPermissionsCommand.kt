package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.user.factionUser
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

    fun process(player: Player, rank: FactionRank): CommandProcessResult {
        val userFaction = player.factionUser().faction()
            ?: return notInFaction()

        if (userFaction.id.value != rank.factionId) {
            return invalidRank()
        }

        player.sendCommandResult(editPermissionsHeader("rankName" to rank.name))

        val permissionDetails = rank.permissions().map {
            permissionDetails(
                "rankName" to rank.name,
                "permission" to it.permission,
                "value" to it.allowed.toString()
            )
        }

        permissionDetails.forEach { player.sendCommandResult(it) }
        return rankEdited()
    }
}