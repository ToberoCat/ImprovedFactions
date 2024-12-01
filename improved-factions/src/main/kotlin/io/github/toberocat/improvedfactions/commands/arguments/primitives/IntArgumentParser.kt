package io.github.toberocat.improvedfactions.commands.arguments.primitives

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.translation.resolveLocalization
import org.bukkit.command.CommandSender

@Localization("base.arguments.int.error")
class IntArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>) =
        arg.toIntOrNull() ?: throw ArgumentParsingException("base.arguments.int.error")

    override fun tabComplete(pCtx: ParsingContext): List<String> =
        listOf(pCtx.resolveVariableName())
}