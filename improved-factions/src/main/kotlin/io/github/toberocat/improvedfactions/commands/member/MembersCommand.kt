package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.text.SimpleDateFormat

@GeneratedCommandMeta(
    label = "members",
    category = CommandCategory.MEMBER_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("membersHeader"),
        CommandResponse("memberDetail"),
        CommandResponse("notInFaction")
    ]
)
abstract class MembersCommand : MembersCommandContext() {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    fun process(player: Player): CommandProcessResult {
        val faction = player.factionUser().faction()
            ?: return notInFaction()

        player.sendCommandResult(membersHeader())

        val details = loggedTransaction {
            faction.members().map { member ->
                val offlinePlayer = member.offlinePlayer()
                memberDetail(
                    "name" to (offlinePlayer.name ?: "Unknown"),
                    "lastSeen" to getLastSeen(offlinePlayer),
                    "rank" to member.rank().name
                )
            }
        }

        details.dropLast(1).forEach { player.sendCommandResult(it) }
        return details.lastOrNull() ?: membersHeader()
    }

    private fun getLastSeen(member: OfflinePlayer): String = when {
        member.isOnline -> "§aOnline"
        else -> "§e${dateFormat.format(member.lastPlayed)}"
    }
}