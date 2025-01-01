package io.github.toberocat.improvedfactions.commands.claim

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.claims.ClaimStatistics
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.exceptions.NotInFactionException
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "unclaim",
    category = CommandCategory.CLAIM_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("unclaimed"),
        CommandResponse("unclaimedRadius"),
        CommandResponse("notInFaction"),
        CommandResponse("noPermission")
    ]
)
abstract class UnclaimCommand : UnclaimCommandContext() {

    fun process(player: Player, radius: Int?): CommandProcessResult {
        val factionUser = player.factionUser()
        if (!factionUser.isInFaction()) {
            return notInFaction()
        }

        if (!factionUser.hasPermission(Permissions.MANAGE_CLAIMS)) {
            return noPermission()
        }

        val faction = factionUser.faction() ?: throw NotInFactionException()
        if (radius == null) {
            faction.unclaim(player.location.chunk)
            return unclaimed()
        }

        val statistics = faction.unclaimSquare(player.location.chunk, radius) { e ->
            player.sendLocalized(e.key, e.placeholders)
        }

        return unclaimedRadius(
            "radius" to radius.toString(),
            "successful-claims" to statistics.successfulClaims.toString(),
            "total-claims" to statistics.totalClaims.toString()
        )
    }
}