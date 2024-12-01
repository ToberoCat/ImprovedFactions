package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandConfirmation
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@CommandConfirmation
@GeneratedCommandMeta(
    label = "leave",
    category = CommandCategory.MANAGE_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("factionLeft"),
        CommandResponse("notInFaction"),
        CommandResponse("factionDeleted"),
    ]
)
abstract class LeaveCommand : LeaveCommandContext() {
    fun process(player: Player): CommandProcessResult {
        val faction = player.factionUser().faction()
            ?: return notInFaction()

        faction.leave(player.uniqueId)
        return factionLeft("factionName" to faction.name)
    }
}
