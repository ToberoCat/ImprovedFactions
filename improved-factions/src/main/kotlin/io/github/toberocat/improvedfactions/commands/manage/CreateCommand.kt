package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */

@GeneratedCommandMeta(
    label = "create",
    category = CommandCategory.MANAGE_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("createdFaction"),
        CommandResponse("factionAlreadyExists"),
        CommandResponse("alreadyInFaction"),
        CommandResponse("invalidName"),
        CommandResponse("nameTooLong"),
    ]
)
abstract class CreateCommand : CreateCommandContext() {

    fun process(player: Player, name: String) = createFaction(player, name)

    fun process(sender: CommandSender, owner: OfflinePlayer, name: String) = createFaction(owner, name)

    private fun createFaction(owner: OfflinePlayer, name: String): CommandProcessResult {
        if (FactionHandler.getFaction(name) != null) {
            return factionAlreadyExists()
        }

        if (owner.factionUser().isInFaction()) {
            return alreadyInFaction()
        }

        if (!BaseModule.config.factionNameRegex.matches(name)) {
            return invalidName()
        }

        if (name.length > BaseModule.config.maxNameLength) {
            return nameTooLong("max" to BaseModule.config.maxNameLength.toString())
        }

        val faction: Faction = FactionHandler.createFaction(owner.uniqueId, name)
        return createdFaction("faction" to faction.name)
    }
}