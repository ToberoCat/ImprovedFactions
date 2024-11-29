package io.github.toberocat.improvedfactions.commands.general

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.allies
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule.enemies
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.OptionalFactionArgument
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionExistOption
import io.github.toberocat.improvedfactions.utils.options.addFactionNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

const val INFO_COMMAND_DESCRIPTION = "base.command.info.description"

@CommandMeta(
    description = INFO_COMMAND_DESCRIPTION
)
open class InfoCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("info") {
    override fun options(): Options = Options.getFromConfig(plugin, "info") { options, _ ->
        options.addFactionNameOption(0)
            .cmdOpt(FactionExistOption(0, true))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        OptionalFactionArgument()
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        val faction = parseArgs(player, args).get<Faction>(0) ?: return false
        player.sendLocalized("base.command.info.header", mapOf(
            "faction" to faction.name
        ))

        loggedTransaction {
            player.showDetails("Members", faction.members().count().toString(), "/f members")
            player.showDetails("Ranks", faction.listRanks().count().toString(), "/f rank")
            player.showDetails("Claims", faction.claims().count().toString(), "/f map")
            player.showDetails("Join Type", faction.factionJoinType.name.lowercase())
            if (PowerRaidsModule.powerRaidModule().isEnabled) {
                player.showDetails("Power", faction.accumulatedPower.toString(), "/f power")
            }
            if (RelationsModule.isEnabled) {
                player.showDetails("Allies", faction.allies().count().toString(), "/f allies")
                player.showDetails("Enemies", faction.enemies().count().toString(), "/f enemies")
            }
        }

        return true
    }

    private fun Player.showDetails(key: String, value: String, cmd: String = "") {
        sendLocalized("base.command.info.detail", mapOf(
            "cmd" to cmd,
            "key" to key,
            "value" to value
        ))
    }
}