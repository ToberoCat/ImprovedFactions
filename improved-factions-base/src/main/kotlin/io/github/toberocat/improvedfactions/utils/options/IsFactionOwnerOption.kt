package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.PlayerOption
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction 

/**
 * Created: 05.08.2023
 * @author Tobias Madlberger (Tobias)
 */
class IsFactionOwnerOption : PlayerOption {
    override fun executePlayer(player: Player, args: Array<out String>): Array<out String> {
        loggedTransaction {
            if (player.uniqueId != player.factionUser().faction()?.owner)
                throw CommandException("base.exceptions.needs-to-be-faction-owner", emptyMap())
        }
        return args
    }

    override fun show(sender: CommandSender, args: Array<out String>): Boolean {
        val player = sender as? Player ?: return false
        return player.uniqueId == loggedTransaction { player.factionUser().faction()?.owner }
    }
}