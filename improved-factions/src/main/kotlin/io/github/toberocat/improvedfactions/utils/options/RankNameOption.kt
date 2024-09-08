package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.ranks.anyRank
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction 

class RankNameOption(index: Int) : PlayerArgumentOptions(index) {
    override fun validate(player: Player, arg: String): Boolean = loggedTransaction {
        player.factionUser().faction()?.anyRank(arg) != null
    }

    override fun argDoesntMatchKey(): String = "base.exceptions.rank-not-found"
}