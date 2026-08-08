package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.PowerType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "admin power add",
    category = CommandCategory.ADMIN_CATEGORY,
    module = PowerRaidsModule.MODULE_NAME,
    responses = [
        CommandResponse("powerAdded"),
    ]
)
abstract class PowerAddCommand : PowerAddCommandContext() {

    fun process(sender: CommandSender, powerType: PowerType, faction: FactionSnapshot, power: Int): CommandProcessResult? {
        val stage = when (powerType) {
            PowerType.ACCUMULATED -> GameStateCommands.setPower(
                faction.id, accumulated = (faction.accumulatedPower + power).coerceIn(-faction.maxPower, faction.maxPower)
            )
            PowerType.MAXIMUM -> GameStateCommands.setPower(faction.id, maximum = (faction.maxPower + power).coerceAtLeast(0))
        }
        return sender.respondAfter(stage) {
            powerAdded("factionName" to faction.name, "powerType" to powerType.name, "amount" to power.toString())
        }
    }
}
