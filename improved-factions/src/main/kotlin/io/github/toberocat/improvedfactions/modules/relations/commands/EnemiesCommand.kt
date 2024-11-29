package io.github.toberocat.improvedfactions.modules.relations.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.enemies
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.enemies.description",
    module = "relations",
    category = CommandCategory.RELATIONS_CATEGORY
)
class EnemiesCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("enemies") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options
            .cmdOpt(InFactionOption(true))
            .cmdOpt(ArgLengthOption(0))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf()

    override fun handle(player: Player, args: Array<String>) = loggedTransaction {
        val faction = player.factionUser().faction() ?: return@loggedTransaction false
        player.sendLocalized("relations.command.enemies.header")
        val enemies = faction.enemies()
        if (enemies.isEmpty()) {
            player.sendLocalized("relations.command.enemies.no-enemies")
            return@loggedTransaction true
        }
        enemies.forEach {
            player.sendLocalized(
                "relations.command.enemies.detail", mapOf(
                    "name" to (FactionHandler.getFaction(it)?.name ?: "Unknown")
                )
            )
        }
        return@loggedTransaction true
    }
}