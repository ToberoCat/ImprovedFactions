package io.github.toberocat.improvedfactions.commands.admin.zone

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.claims.squareClaimAction
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.zone.Zone
import org.bukkit.entity.Player

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "zone claim",
    category = CommandCategory.ADMIN_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("zoneClaimed"),
        CommandResponse("zoneClaimedRadius"),
        CommandResponse("claimError")
    ]
)
abstract class ZoneClaimCommand : ZoneClaimCommandContext() {

    fun processPlayer(executor: Player, zone: Zone, radius: Int?): CommandProcessResult {
        return claimZone(executor, zone, radius)
    }

    private fun claimZone(player: Player, zone: Zone, radius: Int?): CommandProcessResult {
        val statistics = squareClaimAction(
            player.location.chunk,
            radius ?: 0,
            { zone.claim(it) },
            { error -> error.message?.let { player.sendCommandResult(claimError("error" to it)) } }
        )

        return if (radius != null && radius > 0) {
            zoneClaimedRadius(
                "radius" to radius.toString(),
                "successfulClaims" to statistics.successfulClaims.toString(),
                "totalClaims" to statistics.totalClaims.toString()
            )
        } else {
            zoneClaimed()
        }
    }
}