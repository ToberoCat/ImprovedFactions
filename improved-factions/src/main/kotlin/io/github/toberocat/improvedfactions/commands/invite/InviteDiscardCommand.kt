package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.invites.FactionInvite
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "invitediscard",
    category = CommandCategory.INVITE_CATEGORY,
    module = "core",
    responses = [
        CommandResponse("inviteDiscarded"),
        CommandResponse("invalidInvite")
    ]
)
abstract class InviteDiscardCommand : InviteDiscardCommandContext() {

    fun process(player: Player, invite: FactionInvite): CommandProcessResult {
        val inviteToDelete = FactionInvite.findById(invite.id) ?: return invalidInvite()
        inviteToDelete.delete()
        return inviteDiscarded("inviteId" to invite.id.toString())
    }
}
