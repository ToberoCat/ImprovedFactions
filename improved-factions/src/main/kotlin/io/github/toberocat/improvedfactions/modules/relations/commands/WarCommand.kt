package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.relations.RelationType
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "war",
    category = CommandCategory.RELATIONS_CATEGORY,
    module = "relations",
    responses = [
        CommandResponse("warDeclared"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class WarCommand : WarCommandContext() {

    fun process(player: Player, targetFaction: FactionSnapshot): CommandProcessResult? {
        val user = player.cachedUser()
        val faction = user.faction() ?: return notInFaction()

        if (!user.hasPermission(Permissions.MANAGE_RELATION)) {
            return noPermission()
        }

        require(faction.id != targetFaction.id) { "Cannot declare war on your own faction" }
        require(targetFaction.id !in StorageManager.cache.relations(faction.id, "ALLY")) { "Factions are allied" }
        require(targetFaction.id !in StorageManager.cache.relations(faction.id, "ENEMY")) { "Factions are already enemies" }
        return player.respondAfter(GameStateCommands.createRelation(faction.id, targetFaction.id, RelationType.ENEMY.ordinal)) {
            warDeclared("factionName" to targetFaction.name)
        }
    }
}
