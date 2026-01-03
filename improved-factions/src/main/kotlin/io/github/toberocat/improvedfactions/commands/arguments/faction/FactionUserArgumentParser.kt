package io.github.toberocat.improvedfactions.commands.arguments.faction

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Localization("base.arguments.faction-user.not-player")
@Localization("base.arguments.faction-user.not-found")
class FactionUserArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>): Any {
        val user = (sender as? Player)?.factionUser()
            ?: throw ArgumentParsingException("base.arguments.faction-user.not-player")
        val faction = user.faction()
            ?: throw ArgumentParsingException("base.arguments.faction-user.not-found")

        return faction.members().find {
            it.offlinePlayer().name.equals(arg, ignoreCase = true)
        } ?: throw ArgumentParsingException("base.arguments.faction-user.not-found")
    }

    override fun rawTabComplete(pCtx: ParsingContext): List<String> {
        val user = pCtx.player()?.factionUser() ?: return emptyList()
        val faction = user.faction() ?: return emptyList()

        return faction.members()
            .filter { it.uniqueId != user.uniqueId }
            .mapNotNull { it.offlinePlayer().name }
            .filter { it.startsWith(pCtx.arg, ignoreCase = true) }
    }
}
