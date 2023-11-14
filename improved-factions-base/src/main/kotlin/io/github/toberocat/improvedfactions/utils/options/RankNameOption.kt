package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.ranks.anyRank
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

class RankNameOption(index: Int) : PlayerArgumentOptions(index) {
    override fun validate(player: Player, arg: String): Boolean = transaction {
        player.factionUser().faction()?.anyRank(arg) != null
    }

    override fun argDoesntMatchKey(): String = "base.exceptions.rank-not-found"
}