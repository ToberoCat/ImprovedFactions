package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.utils.Base64Item
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

    fun process(player: Player) = setIcon(player, player)

    fun process(sender: CommandSender, target: Player) = setIcon(sender, target)

    private fun setIcon(sender: CommandSender, player: Player): CommandProcessResult? {
        val item = player.inventory.itemInMainHand.clone()

        if (item.type == Material.AIR) {
            return invalidIcon()
        }

        val factionUser = player.cachedUser()
        val faction =factionUser.faction()
            ?: return factionNeeded()

        if (!factionUser.isFactionOwner()) {
            return notFactionOwner()
        }

        if (!factionUser.hasPermission(Permissions.SET_ICON)) {
            return noPermission()
        }

        val encoded = runCatching { Base64Item.encode(item) }.getOrNull() ?: return invalidIcon()
        if (encoded.length > io.github.toberocat.improvedfactions.modules.base.BaseModule.config.maxFactionIconLength) {
            return invalidIcon()
        }
        return sender.respondAfter(GameStateCommands.setIcon(faction.id, encoded)) {
            setIconSuccess("faction" to faction.name)
        }
    }
}
