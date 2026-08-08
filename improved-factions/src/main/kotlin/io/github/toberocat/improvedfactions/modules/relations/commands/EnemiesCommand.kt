package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "enemies",
    category = CommandCategory.RELATIONS_CATEGORY,
    module = RelationsModule.MODULE_NAME,
    responses = [
        CommandResponse("enemiesHeader"),
        CommandResponse("noEnemies"),
        CommandResponse("enemyDetail"),
        CommandResponse("notInFaction")
    ]
)
abstract class EnemiesCommand : EnemiesCommandContext() {

    fun process(player: Player): CommandProcessResult {
        val faction = player.cachedUser().faction() ?: return notInFaction()

        val enemies = StorageManager.cache.relations(faction.id, "ENEMY")
        if (enemies.isEmpty()) {
            return noEnemies()
        }

        val details = enemies.map { enemyId ->
            val enemyName = StorageManager.cache.faction(enemyId)?.name ?: "Unknown"
            enemyDetail("name" to enemyName)
        }

        player.sendCommandResult(enemiesHeader())
        details.dropLast(1).forEach { player.sendCommandResult(it) }
        return details.last()
    }
}
