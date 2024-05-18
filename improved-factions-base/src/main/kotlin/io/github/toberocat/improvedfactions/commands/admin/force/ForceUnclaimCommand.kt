package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.entity.FactionArgument
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.addFactionNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

@CommandMeta(
    description = "base.command.force.unclaim.description",
    category = CommandCategory.ADMIN_CATEGORY
)
class ForceUnclaimCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("unclaim") {
    override fun options(): Options = Options.getFromConfig(plugin, "force.join") { options, _ ->
        options
            .addFactionNameOption(0)
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        FactionArgument()
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val parsedArgs = parseArgs(player, args)
        val faction = parsedArgs.get<Faction>(0) ?: return false

        transaction { faction.unclaim(player.location.chunk) }
        player.sendLocalized("base.command.force.unclaim.unclaimed")
        return true
    }
}