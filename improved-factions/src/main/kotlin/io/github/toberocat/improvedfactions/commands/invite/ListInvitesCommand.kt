package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "invites",
    category = CommandCategory.INVITE_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("success"),
        CommandResponse("invitesHeader"),
        CommandResponse("inviteDetail"),
        CommandResponse("noInvites")
    ]
)
abstract class ListInvitesCommand : ListInvitesCommandContext() {

    fun process(player: Player): CommandProcessResult {
        val invites = StorageManager.cache.invites(player.uniqueId)

        if (invites.isEmpty()) {
            return noInvites()
        }

        player.sendCommandResult(invitesHeader())

        invites.forEach { invite ->
            val faction = StorageManager.cache.faction(invite.factionId) ?: return@forEach
            val rank = StorageManager.cache.rank(invite.rankId) ?: return@forEach
            player.sendCommandResult(
                inviteDetail(
                    "faction" to faction.name,
                    "rank" to rank.name,
                    "expires" to java.time.Duration.ofMillis(invite.expiresAtEpochMillis - System.currentTimeMillis()).toString(),
                    "id" to invite.id.toString()
                )
            )
        }
        return success()
    }
}
