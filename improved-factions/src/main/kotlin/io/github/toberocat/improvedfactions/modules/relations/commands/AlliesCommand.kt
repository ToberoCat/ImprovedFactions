package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.allies
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "allies",
    category = CommandCategory.RELATIONS_CATEGORY,
    module = RelationsModule.MODULE_NAME,
    responses = [
        CommandResponse("alliesHeader"),
        CommandResponse("noAllies"),
        CommandResponse("allyDetail"),
        CommandResponse("notInFaction")
    ]
)
abstract class AlliesCommand : AlliesCommandContext() {

    fun process(player: Player): CommandProcessResult {
        val faction = player.factionUser().faction() ?: return notInFaction()

        val allies = faction.allies()
        if (allies.isEmpty()) {
            return noAllies()
        }

        val details = allies.map { allyId ->
            val allyName = FactionHandler.getFaction(allyId)?.name ?: "Unknown"
            allyDetail("name" to allyName)
        }

        player.sendCommandResult(alliesHeader())
        details.dropLast(1).forEach { player.sendCommandResult(it) }
        return details.last()
    }
}
