package io.github.toberocat.improvedfactions.commands.claim

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "claim",
    category = CommandCategory.CLAIM_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("claimed"),
        CommandResponse("claimedRadius"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class ClaimCommand : ClaimCommandContext() {
    fun process(player: Player, radius: Int?) = claim(player, radius)

    fun claim(player: Player, radius: Int?): CommandProcessResult? {
        val factionUser = player.cachedUser()
        if (!factionUser.isInFaction()) {
            return notInFaction()
        }

        if (!factionUser.hasPermission(Permissions.MANAGE_CLAIMS)) {
            return noPermission()
        }

        val squareRadius = radius ?: 0
        val faction = factionUser.faction() ?: return notInFaction()
        val center = player.location.chunk
        val keys = buildList {
            for (x in center.x - squareRadius..center.x + squareRadius)
                for (z in center.z - squareRadius..center.z + squareRadius)
                    add(ClaimKey(center.world.name, x, z))
        }
        if (center.world.name !in io.github.toberocat.improvedfactions.modules.base.BaseModule.config.allowedWorlds) return null
        if (keys.any { StorageManager.cache.claim(it)?.factionId?.let { id -> id != io.github.toberocat.improvedfactions.user.noFactionId } == true }) return null
        val config = io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule.config
        val totalCost = keys.indices.sumOf { offset ->
            kotlin.math.floor(config.baseClaimPowerCost * Math.pow(config.claimPowerCostGrowth, (faction.claimCount + offset).toDouble())).toInt()
        }
        if (totalCost > faction.accumulatedPower) return null
        return player.respondAfter(GameStateCommands.claimAll(keys, faction.id, faction.accumulatedPower - totalCost)) {
            when {
            squareRadius > 0 -> claimedRadius(
                "radius" to squareRadius.toString(),
                "successful-claims" to keys.size.toString(),
                "total-claims" to keys.size.toString()
            )
            else -> claimed()
        } }
    }
}
