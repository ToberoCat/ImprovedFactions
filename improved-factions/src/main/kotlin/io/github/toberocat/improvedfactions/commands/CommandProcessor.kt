package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.commands.data.CommandData
import org.bukkit.command.CommandSender

interface CommandProcessor {
    val label: String

    val commandData: CommandData

    fun execute(sender: CommandSender, args: Array<String>): CommandProcessResult?

    fun tabComplete(sender: CommandSender, args: Array<String>): List<String>

    fun canExecute(sender: CommandSender, args: Array<String>) = true
}