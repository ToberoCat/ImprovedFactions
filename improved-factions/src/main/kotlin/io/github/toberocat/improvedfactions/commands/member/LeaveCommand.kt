package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandConfirmation
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.cancelledCommandResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.api.events.FactionLeaveEvent
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.exceptions.PlayerIsOwnerLeaveException
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.entity.Player
import org.bukkit.Bukkit

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
    fun process(player: Player): CommandProcessResult? {
        val faction = player.cachedUser().faction()
            ?: return notInFaction()

        if (faction.owner == player.uniqueId) throw PlayerIsOwnerLeaveException()
        val event = FactionLeaveEvent(faction, player.cachedUser())
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return cancelledCommandResult()
        return player.respondAfter(GameStateCommands.setUserFaction(player.uniqueId, noFactionId, 0)) {
            factionLeft("factionName" to faction.name)
        }
    }
}
