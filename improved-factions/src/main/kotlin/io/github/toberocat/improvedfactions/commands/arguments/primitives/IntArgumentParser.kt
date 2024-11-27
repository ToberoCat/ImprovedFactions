package io.github.toberocat.improvedfactions.commands.arguments.primitives

import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import org.bukkit.command.CommandSender

class IntArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String) = arg.toIntOrNull() ?: throw IllegalArgumentException("Invalid number: $arg")

    override fun tabComplete(sender: CommandSender,argIndex: Int, args: Array<String>): List<String> = listOf("<Number>")
}