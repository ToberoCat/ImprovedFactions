package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.cancelledCommandResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.api.events.FactionCreateEvent
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.factions.defaultFactionRanks
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.OfflinePlayer
import org.bukkit.Bukkit
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

    fun process(player: Player, name: String) = createFaction(player, player, name)

    fun process(sender: CommandSender, owner: OfflinePlayer, name: String) = createFaction(sender, owner, name)

    private fun createFaction(sender: CommandSender, owner: OfflinePlayer, name: String): CommandProcessResult? {
        if (StorageManager.cache.factions().any { it.name.equals(name, ignoreCase = true) }) {
            return factionAlreadyExists()
        }

        if (owner.cachedUser().isInFaction()) {
            return alreadyInFaction()
        }

        if (!BaseModule.config.factionNameRegex.matches(name)) {
            return invalidName()
        }

        if (name.length > BaseModule.config.maxNameLength) {
            return nameTooLong("max" to BaseModule.config.maxNameLength.toString())
        }

        val event = FactionCreateEvent(owner.uniqueId, name)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return cancelledCommandResult()

        val ranks = defaultFactionRanks(
            BaseModule.plugin.config.getConfigurationSection("factions.default-faction-ranks")
        )
        return sender.respondAfter(
            GameStateCommands.createFaction(owner.uniqueId, name, 50, ranks, Permissions.knownPermissions.keys)
        ) { factionId ->
            io.github.toberocat.improvedfactions.factions.FactionHandler.createListenersFor(factionId)
            createdFaction("faction" to name)
        }
    }
}
