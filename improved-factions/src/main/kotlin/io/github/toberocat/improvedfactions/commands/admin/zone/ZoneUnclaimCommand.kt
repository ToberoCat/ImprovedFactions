package io.github.toberocat.improvedfactions.commands.admin.zone

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.bukkit.entity.Player

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "admin zone unclaim",
    category = CommandCategory.ADMIN_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("zoneUnclaimed"),
        CommandResponse("zoneUnclaimedRadius"),
        CommandResponse("unclaimError")
    ]
)
abstract class ZoneUnclaimCommand : ZoneUnclaimCommandContext() {

    fun processPlayer(executor: Player, radius: Int): CommandProcessResult? {
        return unclaimZone(executor, radius)
    }

    private fun unclaimZone(player: Player, radius: Int?): CommandProcessResult? {
        val center = player.location.chunk
        val distance = radius ?: 0
        val keys = buildList {
            for (x in center.x - distance..center.x + distance)
                for (z in center.z - distance..center.z + distance) add(ClaimKey(center.world.name, x, z))
        }
        return player.respondAfter(GameStateCommands.unclaimZone(keys, ZoneHandler.FACTION_ZONE_TYPE)) { changed -> if (radius != null && radius > 0) {
            zoneUnclaimedRadius(
                "radius" to radius.toString(),
                "successfulClaims" to changed.toString(),
                "totalClaims" to keys.size.toString()
            )
        } else {
            zoneUnclaimed()
        } }
    }
}
