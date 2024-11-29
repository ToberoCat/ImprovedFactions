package io.github.toberocat.improvedfactions.commands.arguments.bukkit

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Localization("base.arguments.player.usage")
@Localization("base.arguments.player.description")
@Localization("base.arguments.player.error")
class PlayerArgumentParser(
    override val usage: String = "base.arguments.player.usage",
    override val description: String = "base.arguments.player.description",
) : ArgumentParser {

    override fun parse(sender: CommandSender, arg: String): Player =
        Bukkit.getPlayer(arg) ?: throw ArgumentParsingException("base.arguments.player.error")

    override fun tabComplete(sender: CommandSender, argIndex: Int, args: Array<String>) =
        Bukkit.getOnlinePlayers().mapNotNull { it.name }
}