package io.github.toberocat.improvedfactions.commands.claim

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.ClaimStatistics
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
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

@CommandMeta(
    description = "base.command.unclaim.description",
    category = CommandCategory.CLAIM_CATEGORY
)
class UnclaimCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("unclaim") {
    override fun options(): Options = Options.getFromConfig(plugin, "unclaim") { options, _ ->
        options.opt(InFactionOption(true))
            .cmdOpt(FactionPermissionOption(Permissions.MANAGE_CLAIMS))
    }

    override fun arguments() = arrayOf(
        ClaimRadiusArgument()
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val squareRadius = parseArgs(player, args).get<Int>(0) ?: 0

        var statistics = ClaimStatistics(0, 0)
        transaction {
            val faction = player.factionUser().faction() ?: throw NotInFactionException()
            statistics = faction.unclaimSquare(player.location.chunk, squareRadius)
        }

        if (squareRadius > 0) {
            player.sendLocalized("base.command.unclaim.unclaimed-radius", mapOf(
                "radius" to squareRadius.toString(),
                "successful-claims" to statistics.successfulClaims.toString(),
                "total-claims" to statistics.totalClaims.toString()

            ))
        } else {
            player.sendLocalized("base.command.unclaim.unclaimed")
        }
        return true
    }
}