package io.github.toberocat.improvedfactions.modules.gui.commands.core

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.INFO_COMMAND_DESCRIPTION
import io.github.toberocat.improvedfactions.commands.InfoCommand
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import org.bukkit.entity.Player

@CommandMeta(
    description = INFO_COMMAND_DESCRIPTION
)
class GuiInfoCommand(private val guiEngineApi: GuiEngineApi,
                     plugin: ImprovedFactionsPlugin
) : InfoCommand(plugin) {
    override fun handle(player: Player, args: Array<String>): Boolean {
        val faction = parseArgs(player, args).get<Faction>(0) ?: return false
        guiEngineApi.openGui(
            player, "faction-detail-page", mapOf(
                "faction" to faction.name
            )
        )
        return true
    }
}