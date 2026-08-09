package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.relations.RelationType
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.LocalizedException
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "war",
    category = CommandCategory.RELATIONS_CATEGORY,
    module = "relations",
    responses = [
        CommandResponse("warDeclared"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission"),
        CommandResponse("cantDeclareWarOnYourself", "relations.exceptions.cant-declare-war-on-yourself"),
        CommandResponse("alreadyAllied", "relations.exceptions.already-allied"),
        CommandResponse("alreadyEnemy", "relations.exceptions.already-enemy")
    ]
)
abstract class WarCommand : WarCommandContext() {

    fun process(player: Player, targetFaction: FactionSnapshot): CommandProcessResult? {
        val user = player.cachedUser()
        val faction = user.faction() ?: return notInFaction()

        if (!user.hasPermission(Permissions.MANAGE_RELATION)) {
            return noPermission()
        }

        if (faction.id == targetFaction.id) throw LocalizedException("relations.exceptions.cant-declare-war-on-yourself")
        if (targetFaction.id in StorageManager.cache.relations(faction.id, "ALLY")) throw LocalizedException("relations.exceptions.already-allied")
        if (targetFaction.id in StorageManager.cache.relations(faction.id, "ENEMY")) throw LocalizedException("relations.exceptions.already-enemy")
        return player.respondAfter(GameStateCommands.createRelation(faction.id, targetFaction.id, RelationType.ENEMY.ordinal)) {
            warDeclared("factionName" to targetFaction.name)
        }
    }
}
