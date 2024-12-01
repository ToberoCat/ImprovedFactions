package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "icon",
    category = CommandCategory.MANAGE_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("setIconSuccess"),
        CommandResponse("invalidIcon"),
        CommandResponse("factionNeeded"),
        CommandResponse("notFactionOwner"),
        CommandResponse("noPermission")
    ]
)
abstract class IconCommand : IconCommandContext() {

    fun process(player: Player) = setIcon(player)

    fun process(sender: CommandSender, target: Player) = setIcon(target)

    private fun setIcon(player: Player): CommandProcessResult {
        val item = player.inventory.itemInMainHand.clone()

        if (item.type == Material.AIR) {
            return invalidIcon()
        }

        val factionUser = player.factionUser()
        val faction =factionUser.faction()
            ?: return factionNeeded()

        if (!factionUser.isFactionOwner()) {
            return notFactionOwner()
        }

        if (factionUser.hasPermission(Permissions.SET_ICON)) {
            return noPermission()
        }

        faction.icon = item
        return setIconSuccess("faction" to faction.name)
    }
}