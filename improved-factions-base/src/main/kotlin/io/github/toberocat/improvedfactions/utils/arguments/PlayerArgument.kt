package io.github.toberocat.improvedfactions.utils.arguments

import io.github.toberocat.toberocore.command.arguments.Argument
import org.bukkit.Bukkit
import org.bukkit.entity.Player

open class PlayerArgument : Argument<Player> {
    override fun parse(player: Player, value: String): Player? = Bukkit.getPlayer(value)

    override fun defaultValue(player: Player): Player = player

    override fun tab(player: Player): List<String> = Bukkit.getOnlinePlayers().map { it.name }

    override fun descriptionKey(): String = "base.command.args.player"

    override fun usage(): String = "<player>"
}