package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "allyaccept",
    category = CommandCategory.RELATIONS_CATEGORY,
    module = RelationsModule.MODULE_NAME,
    responses = [
        CommandResponse("allyAcceptSuccess"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class AllyAcceptCommand : AllyAcceptCommandContext() {

    fun process(player: Player, targetFaction: FactionSnapshot): CommandProcessResult? {
        val user = player.cachedUser()
        val faction = user.faction() ?: return notInFaction()

        if (!user.hasPermission(Permissions.MANAGE_RELATION)) {
            return noPermission()
        }

        require(StorageManager.cache.allyInvite(faction.id, targetFaction.id) != null) { "No alliance invite" }
        return player.respondAfter(GameStateCommands.acceptAllyInvite(faction.id, targetFaction.id)) {
            allyAcceptSuccess("factionName" to targetFaction.name)
        }
    }
}
