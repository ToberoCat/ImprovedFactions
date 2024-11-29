package io.github.toberocat.improvedfactions.commands.arguments.bukkit

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.CommandSender

@Localization("base.arguments.world.description")
class WorldArgumentParser(override val description: String = "base.arguments.world.description") : ArgumentParser {

    override fun parse(sender: CommandSender, arg: String, args: Array<String>): World =
        Bukkit.getWorld(arg) ?: throw ArgumentParsingException("base.arguments.world.error")

    override fun tabComplete(pCtx: ParsingContext) = Bukkit.getWorlds().map { it.name }
}