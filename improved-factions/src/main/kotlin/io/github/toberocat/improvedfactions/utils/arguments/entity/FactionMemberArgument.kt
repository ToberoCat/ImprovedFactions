package io.github.toberocat.improvedfactions.utils.arguments.entity

import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

class FactionMemberArgument : EntityArgument<FactionUser>() {
    override fun collectEntities(player: Player): List<String> {
        val user = player.factionUser()
        return user.faction()?.members()
            ?.filter { user.canManage(it) }
            ?.mapNotNull { it.offlinePlayer().name }
            ?: emptyList()
    }

    override fun find(player: Player, value: String): FactionUser? {
        val user = player.factionUser()
        return user.faction()?.members()
            ?.filter { user.canManage(it) }
            ?.firstOrNull { it.offlinePlayer().name == value }
    }

    override fun descriptionKey(): String = "base.command.args.faction-member"

    override fun usage(): String = "<member>"
}