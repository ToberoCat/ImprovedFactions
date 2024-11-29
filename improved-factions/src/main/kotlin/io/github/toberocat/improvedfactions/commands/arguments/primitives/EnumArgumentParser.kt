package io.github.toberocat.improvedfactions.commands.arguments.primitives

import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import org.bukkit.command.CommandSender

open class EnumArgumentParser<E : Enum<E>>(
    private val clazz: Class<E>,
    override val usage: String,
    override val description: String,
) : ArgumentParser {

    override fun parse(sender: CommandSender, arg: String): E = clazz.enumConstants.first { it.name.equals(arg, true) }
    override fun tabComplete(sender: CommandSender, argIndex: Int, args: Array<String>) =
        clazz.enumConstants.map { it.name.lowercase() }
}