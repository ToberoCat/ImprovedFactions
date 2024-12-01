package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.ranks.anyRank
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "rank default",
    category = CommandCategory.PERMISSION_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("defaultRankSet"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class DefaultRankCommand : DefaultRankCommandContext() {

    fun process(player: Player, rank: FactionRank): CommandProcessResult {
        val user = player.factionUser()

        if (!user.hasPermission(Permissions.MANAGE_PERMISSIONS)) {
            return noPermission()
        }

        val faction = user.faction() ?: return notInFaction()
        faction.defaultRank = rank.id.value
        return defaultRankSet("rankName" to rank.name)
    }
}
