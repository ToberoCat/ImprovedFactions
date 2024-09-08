package io.github.toberocat.improvedfactions.utils.arguments

import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.entity.Player

open class IntegerArgument(private val usage: String,
                           private val descriptionKey: String) : Argument<Int> {
    override fun parse(player: Player, value: String): Int {
        return value.toIntOrNull() ?: throw CommandException("base.command.args.expected-number", emptyMap())
    }

    override fun defaultValue(player: Player): Int = 0

    override fun tab(player: Player): List<String> = listOf(usage())

    override fun descriptionKey(): String = descriptionKey

    override fun usage(): String = usage
}