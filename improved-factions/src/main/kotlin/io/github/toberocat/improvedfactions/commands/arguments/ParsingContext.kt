package io.github.toberocat.improvedfactions.commands.arguments

import io.github.toberocat.improvedfactions.translation.resolveLocalization
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

data class ParsingContext(
    val sender: CommandSender,
    val args: Array<String>,
    val argIndex: Int,
    val variableKey: String
) {
    val arg get() = args[argIndex]

    fun resolveVariableName() = sender.resolveLocalization(variableKey)

    fun player() = sender as? Player
}
