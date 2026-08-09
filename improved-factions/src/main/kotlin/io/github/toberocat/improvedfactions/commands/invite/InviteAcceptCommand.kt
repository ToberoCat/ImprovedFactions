package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.cancelledCommandResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.api.events.FactionJoinEvent
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import org.bukkit.entity.Player
import org.bukkit.Bukkit

@GeneratedCommandMeta(
    label = "inviteaccept",
    category = CommandCategory.INVITE_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("inviteAccepted"),
        CommandResponse("factionDeleted"),
        CommandResponse("alreadyInFaction", "factions.already-in-faction"),
    ]
)
abstract class InviteAcceptCommand : InviteAcceptCommandContext() {

    fun process(player: Player, invite: InviteSnapshot): CommandProcessResult? {
        if (player.cachedUser().isInFaction()) return alreadyInFaction()
        val faction = StorageManager.cache.faction(invite.factionId) ?: return factionDeleted()
        val event = FactionJoinEvent(faction, player.cachedUser())
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return cancelledCommandResult()
        return player.respondAfter(
            GameStateCommands.acceptInvite(
                invite.id, player.uniqueId, invite.factionId, invite.rankId, PowerRaidsModule.config.baseMemberConstant
            )
        ) { inviteAccepted("factionName" to faction.name) }
    }
}
