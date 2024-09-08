package io.github.toberocat.improvedfactions.commands.claim

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.ClaimStatistics
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.exceptions.CantClaimThisChunkException
import io.github.toberocat.improvedfactions.exceptions.NotInFactionException
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.arguments.ClaimRadiusArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.claim.description",
    category = CommandCategory.CLAIM_CATEGORY
)
class ClaimCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("claim") {
    override fun options(): Options = Options.getFromConfig(plugin, "claim") { options, _ ->
        options.cmdOpt(InFactionOption(true))
            .cmdOpt(FactionPermissionOption(Permissions.MANAGE_CLAIMS))
    }

    override fun arguments() = arrayOf(
        ClaimRadiusArgument()
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val squareRadius = parseArgs(player, args).get<Int>(0) ?: 0

        var statistics = ClaimStatistics(0, 0)
        loggedTransaction {
            val faction = player.factionUser().faction() ?: throw NotInFactionException()
            statistics = faction.claimSquare(player.location.chunk, squareRadius) { e ->
                if (squareRadius == 0) {
                    throw e
                }

                if (e is CantClaimThisChunkException) {
                    e.message?.let { player.sendLocalized(it, e.placeholders) }
                }
            }
        }

        if (squareRadius > 0) {
            player.sendLocalized(
                "base.command.claim.claimed-radius", mapOf(
                    "radius" to squareRadius.toString(),
                    "successful-claims" to statistics.successfulClaims.toString(),
                    "total-claims" to statistics.totalClaims.toString()

                )
            )
        } else {
            player.sendLocalized("base.command.claim.claimed")
        }
        return true
    }
}