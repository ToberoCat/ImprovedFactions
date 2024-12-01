package io.github.toberocat.improvedfactions.commands.arguments.faction

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.ranks.anyRank
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Localization("base.arguments.faction-rank.description")
@Localization("base.arguments.faction-rank.not-player")
@Localization("base.arguments.faction-rank.not-found")
class FactionRankArgumentParser(
    override val description: String = "base.arguments.faction-rank.description"
) : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>): Any {
        val user = (sender as? Player)?.factionUser() ?: throw ArgumentParsingException("base.arguments.faction-rank.not-player")
        return user.faction()?.anyRank(arg) ?: throw ArgumentParsingException("base.arguments.faction-rank.not-found")
    }

    override fun tabComplete(pCtx: ParsingContext): List<String> {
        val user = pCtx.player()?.factionUser() ?: return emptyList()
        return user.faction()
            ?.listRanks()
            ?.filter { user.canManage(it) }
            ?.map { it.name } ?: emptyList()
    }
}