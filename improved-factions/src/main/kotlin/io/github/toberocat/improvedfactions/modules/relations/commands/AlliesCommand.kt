package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.allies
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.addFactionNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "relations.command.allies.description",
    module = "relations",
    category = CommandCategory.RELATIONS_CATEGORY
)
class AlliesCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("allies") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options
            .cmdOpt(InFactionOption(true))
            .cmdOpt(ArgLengthOption(0))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf()

    override fun handle(player: Player, args: Array<String>) = loggedTransaction {
        val faction = player.factionUser().faction() ?: return@loggedTransaction false
        player.sendLocalized("relations.command.allies.header")
        val allies = faction.allies()
        if (allies.isEmpty()) {
            player.sendLocalized("relations.command.allies.no-allies")
            return@loggedTransaction true
        }
        allies.forEach {
            player.sendLocalized(
                "relations.command.allies.detail", mapOf(
                    "name" to (FactionHandler.getFaction(it)?.name ?: "Unknown")
                )
            )
        }
        return@loggedTransaction true
    }
}