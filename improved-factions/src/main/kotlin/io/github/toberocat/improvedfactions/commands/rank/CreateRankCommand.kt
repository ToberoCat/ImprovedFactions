package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "rank create",
    category = CommandCategory.PERMISSION_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("rankCreated"),
        CommandResponse("notInFaction"),
        CommandResponse("invalidPriority"),
        CommandResponse("invalidRankName"),
        CommandResponse("noPermission")
    ]
)
abstract class CreateRankCommand : CreateRankCommandContext() {

    fun process(player: Player, rankName: String, priority: Int): CommandProcessResult {
        val user = player.factionUser()
        if (!user.hasPermission(Permissions.MANAGE_PERMISSIONS)) {
            return noPermission()
        }

        val faction = user.faction() ?: return notInFaction()

        if (rankName.isBlank() || !BaseModule.config.rankNameRegex.matches(rankName)) {
            return invalidRankName()
        }

        if (priority < 0) {
            return invalidPriority("priority" to priority.toString())
        }

        FactionRankHandler.createRank(faction.id.value, rankName, priority, emptyList())
        return rankCreated(
            "rankName" to rankName,
            "priority" to priority.toString()
        )
    }
}
