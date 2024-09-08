package io.github.toberocat.improvedfactions.utils.arguments

import io.github.toberocat.improvedfactions.utils.getOfflinePlayerByName
import io.github.toberocat.toberocore.command.arguments.Argument
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

open class OfflinePlayerArgument : Argument<OfflinePlayer> {
    override fun parse(player: Player, value: String): OfflinePlayer? = value.getOfflinePlayerByName()

    override fun defaultValue(player: Player): OfflinePlayer = player

    override fun tab(player: Player): List<String> = Bukkit.getOfflinePlayers().mapNotNull { it.name }

    override fun descriptionKey(): String = "base.command.args.player"

    override fun usage(): String = "<player>"
}