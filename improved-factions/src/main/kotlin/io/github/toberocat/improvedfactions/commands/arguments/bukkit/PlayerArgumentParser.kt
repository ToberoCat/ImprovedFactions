package io.github.toberocat.improvedfactions.commands.arguments.bukkit

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Localization("base.arguments.player.description")
@Localization("base.arguments.player.error")
class PlayerArgumentParser(
    override val description: String = "base.arguments.player.description",
) : ArgumentParser {

    override fun parse(sender: CommandSender, arg: String, args: Array<String>): Player =
        Bukkit.getPlayer(arg) ?: throw ArgumentParsingException("base.arguments.player.error")

    override fun tabComplete(pCtx: ParsingContext) =
        Bukkit.getOnlinePlayers().mapNotNull { it.name }
}