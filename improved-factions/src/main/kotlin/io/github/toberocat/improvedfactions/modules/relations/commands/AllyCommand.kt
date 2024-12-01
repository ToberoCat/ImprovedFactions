package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.inviteToAlliance
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "ally",
    category = CommandCategory.RELATIONS_CATEGORY,
    module = "relations",
    responses = [
        CommandResponse("allyInviteSuccess"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class AllyCommand : AllyCommandContext() {

    fun process(player: Player, targetFaction: Faction): CommandProcessResult {
        val faction = player.factionUser().faction() ?: return notInFaction()

        if (!player.factionUser().hasPermission(Permissions.MANAGE_RELATION)) {
            return noPermission()
        }

        faction.inviteToAlliance(targetFaction)
        return allyInviteSuccess("factionName" to targetFaction.name)
    }
}