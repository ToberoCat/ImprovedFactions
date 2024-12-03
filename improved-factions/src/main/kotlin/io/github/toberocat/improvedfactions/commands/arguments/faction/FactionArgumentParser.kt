package io.github.toberocat.improvedfactions.commands.arguments.faction

import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import org.bukkit.command.CommandSender

class FactionArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>): Faction {
        return FactionHandler.getFaction(arg) ?: throw ArgumentParsingException("base.arguments.faction.error")
    }

    override fun rawTabComplete(pCtx: ParsingContext) =
        FactionHandler.getFactions().map { it.name }
}