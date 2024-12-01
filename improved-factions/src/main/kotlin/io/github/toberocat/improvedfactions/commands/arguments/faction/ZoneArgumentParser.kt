package io.github.toberocat.improvedfactions.commands.arguments.faction

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.bukkit.command.CommandSender

class ZoneArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>) =
        ZoneHandler.getZone(arg) ?: throw ArgumentParsingException("Zone $arg not found.")

    override fun tabComplete(pCtx: ParsingContext) = ZoneHandler.getZones().toList()
}