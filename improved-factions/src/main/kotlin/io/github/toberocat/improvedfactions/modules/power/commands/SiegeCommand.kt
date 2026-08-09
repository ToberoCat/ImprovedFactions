package io.github.toberocat.improvedfactions.modules.power.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.claimKey
import io.github.toberocat.improvedfactions.claims.overclaim.ClaimSiegeManager
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "siege",
    category = CommandCategory.POWER_CATEGORY,
    module = PowerRaidsModule.MODULE_NAME,
    responses = [
        CommandResponse("notInClaim"),
        CommandResponse("ownClaim"),
        CommandResponse("notRaidable"),
        CommandResponse("siegeStarted")
    ]
)
abstract class SiegeCommand: SiegeCommandContext() {

    fun process(player: Player): CommandProcessResult {
        val claim = StorageManager.cache.claim(player.location.claimKey())
        if (claim == null || claim.factionId == noFactionId) {
            return notInClaim()
        }
        if (claim.factionId == StorageManager.cache.user(player.uniqueId)?.factionId) {
            return ownClaim()
        }
        if (!claim.isRaidable) {
            return notRaidable()
        }

        ClaimSiegeManager.getManager(claim).startSiege(player)
        return siegeStarted("factionName" to (StorageManager.cache.faction(claim.factionId)?.name ?: "Unknown"))
    }
}
