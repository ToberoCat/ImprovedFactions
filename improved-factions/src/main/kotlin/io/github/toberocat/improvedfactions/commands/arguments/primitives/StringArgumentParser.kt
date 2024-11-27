package io.github.toberocat.improvedfactions.commands.arguments.primitives

import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import org.bukkit.command.CommandSender

class StringArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String) = arg

    override fun tabComplete(sender: CommandSender, argIndex: Int, args: Array<String>): List<String> = emptyList()
}