package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionArgument
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.addFactionNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.force.claim.description",
    module = "core",
    category = CommandCategory.GENERAL_CATEGORY
)
class ForceClaimCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("claim") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options
            .addFactionNameOption(0)
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        FactionArgument()
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val parsedArgs = parseArgs(player, args)
        val faction = parsedArgs.get<Faction>(0) ?: return false

        loggedTransaction { faction.claim(player.location.chunk) }
        player.sendLocalized("base.command.force.claim.claimed")
        return true
    }
}