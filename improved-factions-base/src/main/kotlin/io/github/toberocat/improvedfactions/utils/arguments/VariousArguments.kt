package io.github.toberocat.improvedfactions.utils.arguments

import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.entity.RankArgument
import org.bukkit.entity.Player
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction 

class PowerArgument : IntegerArgument("<power>", "base.command.args.power")
class PriorityArgument : IntegerArgument("<priority>", "base.command.args.priority")
class FallbackRankArgument : RankArgument() {
    override fun defaultValue(player: Player): FactionRank? = loggedTransaction {
        player.factionUser().faction()?.defaultRank?.let { FactionRank.findById(it) }
    }

    override fun descriptionKey(): String = "base.command.args.fallback-rank"

    override fun usage(): String = "[fallback]"
}