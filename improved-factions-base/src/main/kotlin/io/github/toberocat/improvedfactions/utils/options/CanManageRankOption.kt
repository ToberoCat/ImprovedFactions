package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.ranks.anyRank
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction 

class CanManageRankOption(index: Int) : PlayerArgumentOptions(index) {
    override fun validate(player: Player, arg: String): Boolean = loggedTransaction {
        val user = player.factionUser()
        val faction = user.faction() ?: return@loggedTransaction false
        return@loggedTransaction user.canManage(faction.anyRank(arg) ?: return@loggedTransaction false)
    }

    override fun argDoesntMatchKey(): String = "base.exceptions.cant-manage-this-rank"
}