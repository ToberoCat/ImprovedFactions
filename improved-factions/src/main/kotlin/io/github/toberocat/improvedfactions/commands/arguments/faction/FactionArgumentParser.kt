package io.github.toberocat.improvedfactions.commands.arguments.faction

import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import org.bukkit.command.CommandSender

class FactionArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>): FactionSnapshot {
        return StorageManager.cache.factions().firstOrNull { it.name.equals(arg, ignoreCase = true) }
            ?: throw ArgumentParsingException("base.arguments.faction.error")
    }

    override fun rawTabComplete(pCtx: ParsingContext) =
        StorageManager.cache.factions().map { it.name }
}
