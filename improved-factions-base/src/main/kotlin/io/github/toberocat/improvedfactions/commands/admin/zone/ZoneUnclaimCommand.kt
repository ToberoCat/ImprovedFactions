package io.github.toberocat.improvedfactions.commands.admin.zone

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.squareClaimAction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.ClaimRadiusArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

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
        transaction {
            squareClaimAction(
                player.location.chunk,
                squareRadius,
                { ZoneHandler.unclaim(it) },
                { player.sendLocalized(it.message ?: "base.command.zone.claim.error") })
        }

        player.sendLocalized("base.command.zone.unclaimed")
        return true
    }
}