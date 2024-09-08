package io.github.toberocat.improvedfactions.utils.arguments.entity

import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.ranks.anyRank
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

open class RankArgument : EntityArgument<FactionRank>() {
    override fun collectEntities(player: Player): List<String> {
        val user = player.factionUser()
        val faction = user.faction() ?: return emptyList()
        return faction.listRanks()
            .filter { user.canManage(it) }
            .map { it.name }
    }

    override fun find(player: Player, value: String): FactionRank? {
        val user = player.factionUser()
        val faction = user.faction() ?: return null
        val rank = faction.anyRank(value)
        return when {
            rank != null && user.canManage(rank) -> rank
            else -> null
        }
    }

    override fun descriptionKey(): String = "base.command.args.rank"

    override fun usage(): String = "<RankName>"
}