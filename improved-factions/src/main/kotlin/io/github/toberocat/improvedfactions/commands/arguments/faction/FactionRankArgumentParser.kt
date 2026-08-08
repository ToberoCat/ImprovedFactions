package io.github.toberocat.improvedfactions.commands.arguments.faction

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.database.storage.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Localization("base.arguments.faction-rank.not-player")
@Localization("base.arguments.faction-rank.not-found")
class FactionRankArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>): Any {
        val user = (sender as? Player)?.cachedUser() ?: throw ArgumentParsingException("base.arguments.faction-rank.not-player")
        return StorageManager.cache.ranks(user.factionId).firstOrNull { it.name.equals(arg, ignoreCase = true) }
            ?: throw ArgumentParsingException("base.arguments.faction-rank.not-found")
    }

    override fun rawTabComplete(pCtx: ParsingContext): List<String> {
        val user = pCtx.player()?.cachedUser() ?: return emptyList()
        return StorageManager.cache.ranks(user.factionId).filter(user::canManage).map { it.name }
    }
}
