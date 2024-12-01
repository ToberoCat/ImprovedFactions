package io.github.toberocat.improvedfactions.commands.arguments.faction

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.invites.FactionInvite
import io.github.toberocat.improvedfactions.invites.invites
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Localization("base.arguments.faction-invite.not-player")
@Localization("base.arguments.faction-invite.not-found")
@Localization("base.arguments.faction-invite.not-invited")
class FactionInviteArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>): FactionInvite {
        if (sender !is Player) {
            throw ArgumentParsingException("base.arguments.faction-invite.not-player")
        }

        val faction = FactionHandler.getFaction(arg)
            ?: throw ArgumentParsingException("base.arguments.faction-invite.not-found")

        val invite = sender.factionUser().invites().firstOrNull { it.factionId == faction.id.value }
            ?: throw ArgumentParsingException("base.arguments.faction-invite.not-invited")
        return invite
    }

    override fun tabComplete(pCtx: ParsingContext) = (pCtx.sender as? Player)
        ?.factionUser()
        ?.invites()
        ?.mapNotNull { FactionHandler.getFaction(it.factionId)?.name }
        ?: emptyList()
}