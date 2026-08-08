package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "invitediscard",
    category = CommandCategory.INVITE_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("inviteDiscarded"),
        CommandResponse("invalidInvite")
    ]
)
abstract class InviteDiscardCommand : InviteDiscardCommandContext() {

    fun process(player: Player, invite: InviteSnapshot): CommandProcessResult? =
        player.respondAfter(GameStateCommands.deleteInvite(invite.id)) {
            inviteDiscarded("inviteId" to invite.id.toString())
        }
}
