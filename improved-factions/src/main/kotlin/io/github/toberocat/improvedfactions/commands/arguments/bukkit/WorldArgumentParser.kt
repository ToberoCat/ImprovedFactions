package io.github.toberocat.improvedfactions.commands.arguments.bukkit

import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.CommandSender

class WorldArgumentParser : ArgumentParser {

    override fun parse(sender: CommandSender, arg: String, args: Array<String>): World =
        Bukkit.getWorld(arg) ?: throw ArgumentParsingException("base.arguments.world.error")

    override fun rawTabComplete(pCtx: ParsingContext) = Bukkit.getWorlds().map { it.name }
}