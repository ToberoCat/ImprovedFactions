package io.github.toberocat.improvedfactions.commands.arguments

import org.bukkit.command.CommandSender

interface ArgumentParser {
    val usage: String
    val description: String

    fun parse(sender: CommandSender, arg: String): Any

    fun tabComplete(sender: CommandSender, argIndex: Int, args: Array<String>): List<String>
}