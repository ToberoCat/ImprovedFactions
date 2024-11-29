package io.github.toberocat.improvedfactions.commands.arguments.primitives

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.translation.resolveLocalization
import org.bukkit.command.CommandSender

@Localization("base.arguments.string.usage")
@Localization("base.arguments.string.description")
open class StringArgumentParser(
    override val usage: String = "base.arguments.string.usage",
    override val description: String = "base.arguments.string.description",
) : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String) = arg

    override fun tabComplete(sender: CommandSender, argIndex: Int, args: Array<String>): List<String> =
        listOf(sender.resolveLocalization(usage))
}