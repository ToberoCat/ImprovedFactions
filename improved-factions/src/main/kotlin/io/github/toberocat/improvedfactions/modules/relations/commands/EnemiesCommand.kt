package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.enemies
import io.github.toberocat.improvedfactions.user.factionUser
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
        val faction = player.factionUser().faction() ?: return notInFaction()

        val enemies = faction.enemies()
        if (enemies.isEmpty()) {
            return noEnemies()
        }

        val details = enemies.map { enemyId ->
            val enemyName = FactionHandler.getFaction(enemyId)?.name ?: "Unknown"
            enemyDetail("name" to enemyName)
        }

        player.sendCommandResult(enemiesHeader())
        details.dropLast(1).forEach { player.sendCommandResult(it) }
        return details.last()
    }
}