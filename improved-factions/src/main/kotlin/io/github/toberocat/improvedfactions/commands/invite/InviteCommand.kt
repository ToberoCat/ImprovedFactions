package io.github.toberocat.improvedfactions.commands.invite

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.messages.MessageBroker
import io.github.toberocat.improvedfactions.factions.LocalizedMessage
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.OfflinePlayer
import org.bukkit.Bukkit
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
        rank: RankSnapshot?,
    ): CommandProcessResult? {
        val user = inviter.cachedUser()
        val faction = user.faction() ?: return playerNoFaction()

        if (!user.hasPermission(Permissions.SEND_INVITES)) {
            return noPermission()
        }

        val factionRank = rank ?: StorageManager.cache.rank(faction.defaultRankId) ?: return rankNotFound()
        val invitedId = invited.uniqueId
        val invitedName = invited.name ?: "unknown"
        val inviterName = inviter.displayName
        val stage = GameStateCommands.createInvite(
            inviter.uniqueId, invitedId, faction.id, factionRank.id,
            java.time.Instant.now().plusSeconds(BaseModule.config.inviteExpiresInMinutes * 60L)
        )
        return inviter.respondAfter(stage) {
            MessageBroker.send(faction.id, LocalizedMessage(
                "base.faction.player-invited",
                mapOf("inviter" to inviterName, "invited" to invitedName)
            ))
            Bukkit.getPlayer(invitedId)?.sendCommandResult(playerInvited(
                "faction" to faction.name, "inviter" to inviterName, "rank" to factionRank.name
            ))
            invitedPlayer("player" to invitedName)
        }
    }
}
