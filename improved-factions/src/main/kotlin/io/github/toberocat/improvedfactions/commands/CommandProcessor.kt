package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.commands.data.CommandData
import io.github.toberocat.improvedfactions.commands.contract.GeneratedCommandContract
import io.github.toberocat.improvedfactions.commands.contract.toGeneratedCommandContract
import org.bukkit.command.CommandSender

interface CommandProcessor {
    val label: String

    val commandData: CommandData

    /** The same generated metadata exposed through the stable command-testing contract. */
    val contract: GeneratedCommandContract
        get() = commandData.toGeneratedCommandContract()

    fun execute(sender: CommandSender, args: Array<String>): CommandProcessResult?

    fun tabComplete(sender: CommandSender, args: Array<String>): List<String>

    fun canExecute(sender: CommandSender, args: Array<String>) = true
}
