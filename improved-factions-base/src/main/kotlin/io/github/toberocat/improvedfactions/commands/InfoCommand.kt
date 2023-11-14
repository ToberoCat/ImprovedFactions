package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.utils.arguments.OptionalFactionArgument
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionExistOption
import io.github.toberocat.improvedfactions.utils.options.addFactionNameOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "base.command.info.description"
)
class InfoCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("info") {
    override fun options(): Options = Options.getFromConfig(plugin, "info") { options, _ ->
        options.addFactionNameOption(0)
            .cmdOpt(FactionExistOption(0, true))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        OptionalFactionArgument()
    )

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val faction = parseArgs(player, args).get<Faction>(0) ?: return false
        plugin.guiEngineApi.openGui(
            player, "faction-detail-page", mapOf(
                "faction" to faction.name
            )
        )
        return true
    }
}