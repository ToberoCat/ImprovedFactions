package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.LocalizedException
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "ally",
    category = CommandCategory.RELATIONS_CATEGORY,
    module = "relations",
    responses = [
        CommandResponse("allyInviteSuccess"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission"),
        CommandResponse("cantAllyYourself", "relations.exceptions.cant-ally-yourself"),
        CommandResponse("alreadyAllied", "relations.exceptions.already-allied"),
        CommandResponse("alreadyEnemy", "relations.exceptions.already-enemy"),
        CommandResponse("alreadyInvited", "relations.exceptions.already-invited")
    ]
)
abstract class AllyCommand : AllyCommandContext() {

    fun process(player: Player, targetFaction: FactionSnapshot): CommandProcessResult? {
        val user = player.cachedUser()
        val faction = user.faction() ?: return notInFaction()

        if (!user.hasPermission(Permissions.MANAGE_RELATION)) {
            return noPermission()
        }

        if (faction.id == targetFaction.id) throw LocalizedException("relations.exceptions.cant-ally-yourself")
        if (targetFaction.id in StorageManager.cache.relations(faction.id, "ALLY")) throw LocalizedException("relations.exceptions.already-allied")
        if (targetFaction.id in StorageManager.cache.relations(faction.id, "ENEMY")) throw LocalizedException("relations.exceptions.already-enemy")
        if (StorageManager.cache.allyInvite(faction.id, targetFaction.id) != null) throw LocalizedException("relations.exceptions.already-invited")
        return player.respondAfter(GameStateCommands.createAllyInvite(
            faction.id, targetFaction.id, java.time.Instant.now().plusSeconds(300)
        )) { allyInviteSuccess("factionName" to targetFaction.name) }
    }
}
