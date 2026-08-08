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

@GeneratedCommandMeta(
    label = "admin power set",
    category = CommandCategory.ADMIN_CATEGORY,
    module = PowerRaidsModule.MODULE_NAME,
    responses = [
        CommandResponse("powerSet"),
        CommandResponse("invalidArguments"),
        CommandResponse("factionNotFound")
    ]
)
abstract class PowerSetCommand : PowerSetCommandContext() {

    fun process(sender: CommandSender, powerType: PowerType, faction: FactionSnapshot, power: Int): CommandProcessResult? {
        val stage = when (powerType) {
            PowerType.ACCUMULATED -> GameStateCommands.setPower(
                faction.id, accumulated = power.coerceIn(-faction.maxPower, faction.maxPower)
            )
            PowerType.MAXIMUM -> GameStateCommands.setPower(faction.id, maximum = power.coerceAtLeast(0))
        }
        return sender.respondAfter(stage) {
            powerSet("factionName" to faction.name, "powerType" to powerType.name, "amount" to power.toString())
        }
    }
}
