package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.invites.FactionInvite
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "inviteaccept",
    category = CommandCategory.INVITE_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("inviteAccepted"),
        CommandResponse("factionDeleted"),
    ]
)
abstract class InviteAcceptCommand : InviteAcceptCommandContext() {

    fun process(player: Player, invite: FactionInvite): CommandProcessResult {
        val faction = Faction.findById(invite.factionId) ?: return factionDeleted()

        invite.delete()
        faction.join(player.uniqueId, invite.rankId)

        return inviteAccepted("factionName" to (Faction.findById(invite.factionId)?.name ?: "Unknown"))
    }
}