package io.github.toberocat.improvedfactions.commands.admin.zone

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.claims.squareClaimAction
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.bukkit.entity.Player

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "zone unclaim",
    category = CommandCategory.ADMIN_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("zoneUnclaimed"),
        CommandResponse("zoneUnclaimedRadius"),
        CommandResponse("unclaimError")
    ]
)
abstract class ZoneUnclaimCommand : ZoneUnclaimCommandContext() {

    fun processPlayer(executor: Player, radius: Int): CommandProcessResult {
        return unclaimZone(executor, radius)
    }

    private fun unclaimZone(player: Player, radius: Int?): CommandProcessResult {
        val statistics = squareClaimAction(
            player.location.chunk,
            radius ?: 0,
            { ZoneHandler.unclaim(it) },
            { error -> player.sendCommandResult(unclaimError("error" to error.key)) }
        )

        return if (radius != null && radius > 0) {
            zoneUnclaimedRadius(
                "radius" to radius.toString(),
                "successfulClaims" to statistics.successfulClaims.toString(),
                "totalClaims" to statistics.totalClaims.toString()
            )
        } else {
            zoneUnclaimed()
        }
    }
}