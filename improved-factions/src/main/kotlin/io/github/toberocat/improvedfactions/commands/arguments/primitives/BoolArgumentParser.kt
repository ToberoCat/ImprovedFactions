package io.github.toberocat.improvedfactions.commands.arguments.primitives

import io.github.toberocat.improvedfactions.annotations.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import org.bukkit.command.CommandSender

@Localization("base.arguments.bool.usage")
@Localization("base.arguments.bool.description")
@Localization("base.arguments.bool.error")
class BoolArgumentParser(
    override val usage: String = "base.arguments.bool.usage",
    override val description: String = "base.arguments.bool.description",
) : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String) =
        arg.toBooleanStrictOrNull() ?: throw ArgumentParsingException("base.arguments.bool.error")

    override fun tabComplete(sender: CommandSender, argIndex: Int, args: Array<String>) = listOf("true", "false")
}