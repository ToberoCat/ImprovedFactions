package io.github.toberocat.improvedfactions.utils.arguments

import io.github.toberocat.improvedfactions.ranks.anyRank
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.entity.Player
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction 

class RankNameInputArgument : Argument<String> {
    override fun parse(player: Player, value: String): String {
        loggedTransaction {
            if (player.factionUser().faction()?.anyRank(value) != null)
                throw CommandException("base.command.args.invalid-rank-name", emptyMap())
        }
        return value
    }

    override fun tab(sender: Player): List<String> = listOf(usage())

    override fun descriptionKey(): String = "base.command.args.rank-name"

    override fun usage(): String = "<RankName>"
}