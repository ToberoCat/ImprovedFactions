package io.github.toberocat.improvedfactions.commands.admin.zone

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.ZoneArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.ZoneExistOption
import io.github.toberocat.improvedfactions.zone.Zone
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

@CommandMeta(
    description = "base.command.zone.claim.description",
    category = CommandCategory.ADMIN_CATEGORY
)
class ZoneClaimCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("claim") {
    override fun options(): Options = Options.getFromConfig(plugin, "claim") { options, _ ->
        options.cmdOpt(ZoneExistOption(0))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        ZoneArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val zone = parseArgs(player, args).get<Zone>(0) ?: return false
        transaction { zone.claim(player.location.chunk) }
        player.sendLocalized("base.command.zone.claimed")
        return true
    }
}