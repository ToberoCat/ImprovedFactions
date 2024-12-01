package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.declareWar
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "war",
    category = CommandCategory.RELATIONS_CATEGORY,
    module = "relations",
    responses = [
        CommandResponse("warDeclared"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class WarCommand : WarCommandContext() {

    fun process(player: Player, targetFaction: Faction): CommandProcessResult {
        val faction = player.factionUser().faction() ?: return notInFaction()

        if (!player.factionUser().hasPermission(Permissions.MANAGE_RELATION)) {
            return noPermission()
        }

        loggedTransaction {
            faction.declareWar(targetFaction)
        }

        return warDeclared("factionName" to targetFaction.name)
    }
}
