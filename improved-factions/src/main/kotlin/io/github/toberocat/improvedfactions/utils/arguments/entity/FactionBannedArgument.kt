package io.github.toberocat.improvedfactions.utils.arguments.entity

import io.github.toberocat.improvedfactions.factions.ban.FactionBan
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

class FactionBannedArgument : EntityArgument<FactionBan>() {
    override fun collectEntities(player: Player): List<String> = player.factionUser().faction()?.bans()
        ?.mapNotNull { it.user.offlinePlayer().name }
        ?: emptyList()

    override fun find(player: Player, value: String): FactionBan? = player.factionUser()
        .faction()?.bans()
        ?.firstOrNull { it.user.offlinePlayer().name == value }


    override fun descriptionKey(): String = "base.command.args.banned-player"

    override fun usage(): String = "<banned>"
}