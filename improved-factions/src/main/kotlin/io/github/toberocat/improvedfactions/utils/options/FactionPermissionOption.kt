package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.PlayerOption
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction 

class FactionPermissionOption(private val permission: String) : PlayerOption {
    override fun executePlayer(player: Player, args: Array<String>): Array<String> {
        loggedTransaction {
            if (!player.factionUser().hasPermission(permission))
                throw CommandException("base.exceptions.missing-permissions", emptyMap())
        }
        return args
    }

    override fun show(sender: CommandSender, args: Array<out String>): Boolean =
        loggedTransaction { (sender as? Player)?.factionUser()?.hasPermission(permission) == true }
}