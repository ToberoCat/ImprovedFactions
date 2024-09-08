package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.list.description"
)
class ListFactionsCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("list") {
    override fun options(): Options = Options.getFromConfig(plugin, label)

    override fun arguments(): Array<Argument<*>> = arrayOf()

    override fun handle(player: Player, args: Array<String>): Boolean {
        player.sendLocalized("base.command.list.header", mapOf())

        loggedTransaction {
            FactionHandler.getFactions().forEach { faction ->
                player.sendLocalized("base.command.list.faction", mapOf(
                    "name" to faction.name,
                    "power" to faction.accumulatedPower.toString(),
                    "members" to faction.members().count().toString()
                ))
            }
        }

        return true
    }
}