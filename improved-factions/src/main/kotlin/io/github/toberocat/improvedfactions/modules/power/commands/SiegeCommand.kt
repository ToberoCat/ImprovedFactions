package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "siege",
    category = CommandCategory.POWER_CATEGORY,
    module = PowerRaidsModule.MODULE_NAME,
    responses = [
        CommandResponse("notInClaim"),
        CommandResponse("ownClaim"),
        CommandResponse("siegeStarted")
    ]
)
abstract class SiegeCommand: SiegeCommandContext() {

    fun process(player: Player): CommandProcessResult {
        val claim = player.location.getFactionClaim()
        if (claim == null || claim.factionId == noFactionId) {
            return notInClaim()
        }
        if (claim.factionId == player.factionUser().factionId) {
            return ownClaim()
        }

        claim.siegeManager.startSiege(player)
        return siegeStarted("factionName" to (claim.faction()?.name ?: "Unknown"))
    }
}