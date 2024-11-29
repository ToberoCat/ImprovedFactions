package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandConfirmation
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


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

    fun process(player: Player): CommandProcessResult {
        return deleteFaction(player)
    }

    fun process(sender: CommandSender, target: OfflinePlayer): CommandProcessResult {
        return deleteFaction(target)
    }

    private fun deleteFaction(player: OfflinePlayer): CommandProcessResult {
        val faction = player.factionUser().faction()
            ?: return notInFaction()

        if (!player.factionUser().faction()?.owner?.equals(player.uniqueId)!!) {
            return notFactionOwner()
        }

        faction.delete()
        return deletedFaction("faction" to faction.name)
    }
}