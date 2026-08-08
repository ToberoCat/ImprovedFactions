package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


@GeneratedCommandMeta(
    label = "rename",
    category = CommandCategory.MANAGE_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("renamedFaction"),
        CommandResponse("factionNeeded"),
        CommandResponse("notFactionOwner"),
        CommandResponse("noPermission"),
        CommandResponse("invalidName"),
        CommandResponse("nameTooLong"),
        CommandResponse("factionNameExists")
    ]
)
abstract class RenameCommand : RenameCommandContext() {

    fun process(player: Player, newName: String) = setFactionName(player, player, newName)

    fun process(sender: CommandSender, target: OfflinePlayer, newName: String) = setFactionName(sender, target, newName)

    private fun setFactionName(sender: CommandSender, player: OfflinePlayer, newName: String): CommandProcessResult? {
        val factionUser = player.cachedUser()
        val faction = factionUser.faction() ?: return factionNeeded()

        if (!factionUser.isFactionOwner()) {
            return notFactionOwner()
        }

        if (!factionUser.hasPermission(Permissions.RENAME_FACTION)) {
            return noPermission()
        }

        if (!BaseModule.config.factionNameRegex.matches(newName)) {
            return invalidName()
        }

        if (newName.length > BaseModule.config.maxNameLength) {
            return nameTooLong("max" to BaseModule.config.maxNameLength.toString())
        }

        if (StorageManager.cache.factions().any { it.name.equals(newName, ignoreCase = true) }) {
            return factionNameExists()
        }

        return sender.respondAfter(GameStateCommands.renameFaction(faction.id, newName)) {
            renamedFaction("faction" to newName)
        }
    }
}
