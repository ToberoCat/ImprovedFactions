package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.annotations.*
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


@CommandConfirmation
@PermissionDefault(true)
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

        loggedTransaction {
            faction.delete()
        }

        return deletedFaction("faction" to faction.name)
    }
}