package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "ally",
    category = CommandCategory.RELATIONS_CATEGORY,
    module = "relations",
    responses = [
        CommandResponse("allyInviteSuccess"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class AllyCommand : AllyCommandContext() {

    fun process(player: Player, targetFaction: FactionSnapshot): CommandProcessResult? {
        val user = player.cachedUser()
        val faction = user.faction() ?: return notInFaction()

        if (!user.hasPermission(Permissions.MANAGE_RELATION)) {
            return noPermission()
        }

        require(faction.id != targetFaction.id) { "Cannot ally your own faction" }
        require(targetFaction.id !in StorageManager.cache.relations(faction.id, "ALLY")) { "Factions are already allied" }
        require(targetFaction.id !in StorageManager.cache.relations(faction.id, "ENEMY")) { "Factions are enemies" }
        require(StorageManager.cache.allyInvite(faction.id, targetFaction.id) == null) { "Alliance invite already exists" }
        return player.respondAfter(GameStateCommands.createAllyInvite(
            faction.id, targetFaction.id, java.time.Instant.now().plusSeconds(300)
        )) { allyInviteSuccess("factionName" to targetFaction.name) }
    }
}
