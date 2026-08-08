package io.github.toberocat.improvedfactions.commands.arguments.faction

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParsingException
import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
import io.github.toberocat.improvedfactions.database.storage.InviteSnapshot
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Localization("base.arguments.shared.not-player")
@Localization("base.arguments.faction-invite.not-found")
@Localization("base.arguments.faction-invite.not-invited")
class FactionInviteArgumentParser : ArgumentParser {
    override fun parse(sender: CommandSender, arg: String, args: Array<String>): InviteSnapshot {
        if (sender !is Player) {
            throw ArgumentParsingException("base.arguments.shared.not-player")
        }

        val faction = StorageManager.cache.factions().firstOrNull { it.name.equals(arg, ignoreCase = true) }
            ?: throw ArgumentParsingException("base.arguments.faction-invite.not-found")

        val invite = StorageManager.cache.invites(sender.uniqueId).firstOrNull { it.factionId == faction.id }
            ?: throw ArgumentParsingException("base.arguments.faction-invite.not-invited")
        return invite
    }

    override fun rawTabComplete(pCtx: ParsingContext) = (pCtx.sender as? Player)
        ?.let { StorageManager.cache.invites(it.uniqueId) }
        ?.mapNotNull { StorageManager.cache.faction(it.factionId)?.name }
        ?: emptyList()
}
