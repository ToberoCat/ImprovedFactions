package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "invite",
    category = CommandCategory.INVITE_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("invitedPlayer"),
        CommandResponse("playerNoFaction"),
        CommandResponse("playerInvited"),
        CommandResponse("noPermission"),
        CommandResponse("rankNotFound")
    ]
)
abstract class InviteCommand : InviteCommandContext() {

    fun process(
        inviter: Player,
        invited: OfflinePlayer,
        rank: FactionRank?,
    ): CommandProcessResult {
        val faction = inviter.factionUser().faction() ?: return playerNoFaction()

        if (!inviter.factionUser().hasPermission(Permissions.SEND_INVITES)) {
            return noPermission()
        }

        val factionRank = rank ?: faction.getDefaultRank()
        faction.invite(inviter.uniqueId, invited.uniqueId, factionRank.id.value)
        inviter.sendCommandResult(
            invitedPlayer("player" to (invited.name ?: "unknown"))
        )

        invited.player?.sendCommandResult(
            playerInvited(
                "faction" to faction.name,
                "inviter" to inviter.displayName,
                "rank" to factionRank.name
            )
        )

        return invitedPlayer("player" to (invited.name ?: "unknown"))
    }
}
