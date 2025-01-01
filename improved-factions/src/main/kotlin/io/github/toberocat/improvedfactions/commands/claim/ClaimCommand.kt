package io.github.toberocat.improvedfactions.commands.claim

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.getUnformattedLocalized
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
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
        val factionUser = player.factionUser()
        if (!factionUser.isInFaction()) {
            return notInFaction()
        }

        if (!factionUser.hasPermission(Permissions.MANAGE_CLAIMS)) {
            return noPermission()
        }

        val squareRadius = radius ?: 0
        val faction = factionUser.faction() as Faction
        var errorCount = 0
        val statistics = faction.claimSquare(player.location.chunk, squareRadius) { e ->
            player.sendLocalized(e.key, e.placeholders)
            errorCount++
        }

        return when {
            errorCount > 0 -> null
            squareRadius > 0 -> claimedRadius(
                "radius" to squareRadius.toString(),
                "successful-claims" to statistics.successfulClaims.toString(),
                "total-claims" to statistics.totalClaims.toString()
            )

            else -> claimed()
        }
    }
}