package io.github.toberocat.improvedfactions.commands.admin.zone

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.squareClaimAction
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.ClaimRadiusArgument
import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.zone.unclaim.description",
    category = CommandCategory.ADMIN_CATEGORY
)
class ZoneUnclaimCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("unclaim") {
    override fun options(): Options = Options.getFromConfig(plugin, label)

    override fun arguments() = arrayOf(
        ClaimRadiusArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val squareRadius = parseArgs(player, args).get<Int>(0) ?: 0
        val statistics = loggedTransaction {
            squareClaimAction(
                player.location.chunk,
                squareRadius,
                { ZoneHandler.unclaim(it) },
                { player.sendLocalized(it.message ?: "base.command.zone.claim.error") })
        }

        if (squareRadius > 0) {
            player.sendLocalized(
                "base.command.zone.unclaimed-radius", mapOf(
                    "radius" to squareRadius.toString(),
                    "successful-claims" to statistics.successfulClaims.toString(),
                    "total-claims" to statistics.totalClaims.toString()

                )
            )
        } else {
            player.sendLocalized("base.command.zone.unclaimed")
        }
        return true
    }
}