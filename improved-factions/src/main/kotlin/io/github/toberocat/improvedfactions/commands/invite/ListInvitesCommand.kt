package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.invites.invites
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.user.factionUser
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
        val invited = player.factionUser()
        val invites = invited.invites()

        if (invites.empty()) {
            return noInvites()
        }

        player.sendCommandResult(invitesHeader())

        invites.forEach { invite ->
            val faction = Faction.findById(invite.factionId) ?: return@forEach
            val rank = FactionRank.findById(invite.rankId) ?: return@forEach
            player.sendCommandResult(
                inviteDetail(
                    "faction" to faction.name,
                    "rank" to rank.name,
                    "expires" to invite.expiresInFormatted(),
                    "id" to invite.id.value.toString()
                )
            )
        }
        return success()
    }
}