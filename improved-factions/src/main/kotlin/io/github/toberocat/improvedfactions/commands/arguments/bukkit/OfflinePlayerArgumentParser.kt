package io.github.toberocat.improvedfactions.commands.arguments.bukkit

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.utils.getOfflinePlayerByName
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

@Localization("base.arguments.player.description")
@Localization("base.arguments.player.error")
class OfflinePlayerArgumentParser(
    override val description: String = "base.arguments.player.description",
) : ArgumentParser {

    override fun parse(sender: CommandSender, arg: String, args: Array<String>): OfflinePlayer =
        arg.getOfflinePlayerByName() ?: throw ArgumentParsingException("base.arguments.player.error")

    override fun tabComplete(pCtx: ParsingContext) =
        Bukkit.getOfflinePlayers().mapNotNull { it.name }
}