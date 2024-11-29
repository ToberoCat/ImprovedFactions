package io.github.toberocat.improvedfactions.commands.arguments.faction

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import org.bukkit.command.CommandSender

@Localization("base.arguments.faction.description")
class FactionArgumentParser(
    override val description: String = "base.arguments.faction.description",
) : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>): Faction {
        return FactionHandler.getFaction(arg) ?: throw ArgumentParsingException("base.arguments.faction.error")
    }

    override fun tabComplete(pCtx: ParsingContext) =
        FactionHandler.getFactions().map { it.name }
}