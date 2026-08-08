package io.github.toberocat.improvedfactions.commands.rank

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.permissions.Permissions
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

    fun process(player: Player, rank: RankSnapshot): CommandProcessResult? {
        val user = player.cachedUser()

        if (!user.hasPermission(Permissions.MANAGE_PERMISSIONS)) {
            return noPermission()
        }

        val faction = user.faction() ?: return notInFaction()
        return player.respondAfter(GameStateCommands.setDefaultRank(faction.id, rank.id)) {
            defaultRankSet("rankName" to rank.name)
        }
    }
}
