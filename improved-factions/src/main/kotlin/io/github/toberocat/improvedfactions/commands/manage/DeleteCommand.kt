package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandConfirmation
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.cancelledCommandResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.api.events.FactionDeleteEvent
import io.github.toberocat.improvedfactions.database.storage.*
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.Bukkit


@CommandConfirmation
@GeneratedCommandMeta(
    label = "delete",
    category = CommandCategory.MANAGE_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("deletedFaction"),
        CommandResponse("notInFaction"),
        CommandResponse("notFactionOwner"),
    ]
)
abstract class DeleteCommand : DeleteCommandContext() {

    fun process(player: Player): CommandProcessResult? {
        return deleteFaction(player, player)
    }

    fun process(sender: CommandSender, target: OfflinePlayer): CommandProcessResult? {
        return deleteFaction(sender, target)
    }

    private fun deleteFaction(sender: CommandSender, player: OfflinePlayer): CommandProcessResult? {
        val user = player.cachedUser()
        val faction = user.faction()
            ?: return notInFaction()

        if (!user.isFactionOwner()) {
            return notFactionOwner()
        }

        val event = FactionDeleteEvent(faction)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return cancelledCommandResult()

        return sender.respondAfter(GameStateCommands.deleteFaction(faction.id)) {
            deletedFaction("faction" to faction.name)
        }
    }
}
