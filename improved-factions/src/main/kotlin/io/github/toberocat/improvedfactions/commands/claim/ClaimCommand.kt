package io.github.toberocat.improvedfactions.commands.claim

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.exceptions.NotEnoughPowerForClaimException
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
        val keys = spiralClaimKeys(center.world.name, center.x, center.z, squareRadius)
        if (center.world.name !in io.github.toberocat.improvedfactions.modules.base.BaseModule.config.allowedWorlds) return null
        if (keys.any { StorageManager.cache.claim(it)?.factionId?.let { id -> id != io.github.toberocat.improvedfactions.user.noFactionId } == true }) return null
        val config = io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule.config
        var totalCost = 0
        var claimedCount = 0
        val affordableKeys = keys.takeWhile {
            val cost = kotlin.math.floor(
                config.baseClaimPowerCost * Math.pow(config.claimPowerCostGrowth, (faction.claimCount + claimedCount).toDouble())
            ).toInt()
            if (totalCost + cost > faction.accumulatedPower) false
            else {
                totalCost += cost
                claimedCount++
                true
            }
        }
        if (affordableKeys.isEmpty()) throw NotEnoughPowerForClaimException(center)
        return player.respondAfter(GameStateCommands.claimAll(affordableKeys, faction.id, faction.accumulatedPower - totalCost)) {
            when {
            squareRadius > 0 -> claimedRadius(
                "radius" to squareRadius.toString(),
                "successful-claims" to affordableKeys.size.toString(),
                "total-claims" to keys.size.toString()
            )
            else -> claimed()
        } }
    }

    /**
     * Traverses each requested chunk exactly once, beginning at the player's chunk and expanding
     * through cardinally adjacent chunks.  If power runs out mid-command, the retained claims are
     * therefore one contiguous spiral instead of an arbitrary corner of the requested square.
     */
    private fun spiralClaimKeys(world: String, centerX: Int, centerZ: Int, radius: Int): List<ClaimKey> {
        val keys = ArrayList<ClaimKey>((radius * 2 + 1) * (radius * 2 + 1))
        keys += ClaimKey(world, centerX, centerZ)
        var x = centerX
        var z = centerZ
        var stepLength = 1
        val directions = arrayOf(1 to 0, 0 to -1, -1 to 0, 0 to 1)

        while (keys.size < (radius * 2 + 1) * (radius * 2 + 1)) {
            directions.forEachIndexed { index, (dx, dz) ->
                repeat(stepLength) {
                    x += dx
                    z += dz
                    if (x in centerX - radius..centerX + radius && z in centerZ - radius..centerZ + radius) {
                        keys += ClaimKey(world, x, z)
                    }
                }
                if (index % 2 == 1) stepLength++
            }
        }
        return keys
    }
}
