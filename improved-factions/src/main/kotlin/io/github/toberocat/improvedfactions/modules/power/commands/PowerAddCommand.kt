package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.factions.PowerAccumulationChangeReason
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.PowerType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "power add",
    category = CommandCategory.ADMIN_CATEGORY,
    module = PowerRaidsModule.MODULE_NAME,
    responses = [
        CommandResponse("powerAdded"),
    ]
)
abstract class PowerAddCommand : PowerAddCommandContext() {

    fun process(sender: CommandSender, powerType: PowerType, faction: Faction, power: Int): CommandProcessResult {
        when (powerType) {
            PowerType.ACCUMULATED -> faction.setAccumulatedPower(
                faction.accumulatedPower + power,
                PowerAccumulationChangeReason.OTHER
            )

            PowerType.MAXIMUM -> faction.setMaxPower(faction.maxPower + power)
        }

        return powerAdded("factionName" to faction.name, "powerType" to powerType.name, "amount" to power.toString())
    }
}
