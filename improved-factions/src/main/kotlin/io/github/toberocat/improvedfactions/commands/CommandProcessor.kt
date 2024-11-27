package io.github.toberocat.improvedfactions.commands

import org.bukkit.command.CommandSender

interface CommandProcessor {
    val label: String

    fun execute(sender: CommandSender, args: Array<String>): CommandProcessResult

    fun tabComplete(sender: CommandSender, args: Array<String>): List<String>
}