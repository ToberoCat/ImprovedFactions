package io.github.toberocat.improvedfactions.commands.admin.zone

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.squareClaimAction
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.ClaimRadiusArgument
import io.github.toberocat.improvedfactions.utils.arguments.ZoneArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.ZoneExistOption
import io.github.toberocat.improvedfactions.zone.Zone
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.zone.claim.description",
    category = CommandCategory.ADMIN_CATEGORY
)
class ZoneClaimCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("claim") {
    override fun options(): Options = Options.getFromConfig(plugin, "claim") { options, _ ->
        options.cmdOpt(ZoneExistOption(0))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        ZoneArgument(),
        ClaimRadiusArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val parsedArgs = parseArgs(player, args)
        val zone = parsedArgs.get<Zone>(0) ?: return false
        val squareRadius = parsedArgs.get<Int>(1) ?: 0
        val statistics = loggedTransaction {
            squareClaimAction(
                player.location.chunk,
                squareRadius,
                { zone.claim(it) },
                { player.sendLocalized(it.message ?: "base.command.zone.claim.error") })
        }

        if (squareRadius > 0) {
            player.sendLocalized(
                "base.command.zone.claimed-radius", mapOf(
                    "radius" to squareRadius.toString(),
                    "successful-claims" to statistics.successfulClaims.toString(),
                    "total-claims" to statistics.totalClaims.toString()

                )
            )
        } else {
            player.sendLocalized("base.command.zone.claimed")
        }
        return true
    }
}