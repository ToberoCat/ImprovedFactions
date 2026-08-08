package io.github.toberocat.improvedfactions.commands.general

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.translation.sendLocalized
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "list",
    category = CommandCategory.GENERAL_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("listHeader"),
        CommandResponse("listFaction"),
        CommandResponse("noFactions")
    ]
)
abstract class ListFactionsCommand : ListFactionsCommandContext() {

    fun process(sender: CommandSender): CommandProcessResult {
        sender.sendCommandResult(listHeader())

        val factions = StorageManager.cache.factions().map { faction ->
            listFaction(
                "name" to faction.name,
                "power" to faction.accumulatedPower.toString(),
                "members" to StorageManager.cache.factionMembers(faction.id).size.toString()
            )
        }

        if (factions.isEmpty()) return noFactions()

        factions.dropLast(1).forEach { sender.sendCommandResult(it) }
        return  factions.last()
    }
}
