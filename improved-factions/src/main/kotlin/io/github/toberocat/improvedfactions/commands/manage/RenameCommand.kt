package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.FactionNameInputArgument
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.utils.options.*
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
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

    fun process(player: Player, newName: String) = setFactionName(player, newName)

    fun process(sender: CommandSender, target: OfflinePlayer, newName: String) = setFactionName(target, newName)

    private fun setFactionName(player: OfflinePlayer, newName: String): CommandProcessResult {
        val factionUser = player.factionUser()
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

        if (FactionHandler.getFaction(newName) != null) {
            return factionNameExists()
        }

        loggedTransaction {
            faction.name = newName
        }

        return renamedFaction("faction" to faction.name)
    }
}