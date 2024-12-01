package io.github.toberocat.improvedfactions.commands.arguments.primitives

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import org.bukkit.command.CommandSender

@Localization("base.arguments.bool.error")
open class BoolArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>) =
        arg.toBooleanStrictOrNull() ?: throw ArgumentParsingException("base.arguments.bool.error")

    override fun tabComplete(pCtx: ParsingContext) = listOf("true", "false")
}