package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.ranks.anyRank
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

class CanManageRankOption(index: Int) : PlayerArgumentOptions(index) {
    override fun validate(player: Player, arg: String): Boolean = transaction {
        val user = player.factionUser()
        val faction = user.faction() ?: return@transaction false
        return@transaction user.canManage(faction.anyRank(arg) ?: return@transaction false)
    }

    override fun argDoesntMatchKey(): String = "base.exceptions.cant-manage-this-rank"
}