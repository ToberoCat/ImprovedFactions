package io.github.toberocat.improvedfactions.commands.arguments.primitives

import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import org.bukkit.command.CommandSender

class BoolArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String) =
        arg.toBooleanStrictOrNull() ?: throw ArgumentParsingException()

    override fun tabComplete(sender: CommandSender, argIndex: Int, args: Array<String>) = listOf("true", "false")
}