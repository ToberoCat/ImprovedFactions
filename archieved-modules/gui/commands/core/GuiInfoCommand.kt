package io.github.toberocat.improvedfactions.modules.gui.commands.core

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.executor.CommandExecutor
import io.github.toberocat.improvedfactions.commands.general.InfoCommandProcessor
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

class GuiInfoCommandProcessor(
    private val guiEngineApi: GuiEngineApi, plugin: ImprovedFactionsPlugin,
    executor: CommandExecutor,
) : InfoCommandProcessor(
    plugin, executor
) {
    override fun process(sender: Player, faction: Faction): CommandProcessResult {
        val actualFaction = sender.factionUser().faction() ?: faction
        guiEngineApi.openGui(
            sender, "faction-detail-page", mapOf(
                "faction" to actualFaction.name
            )
        )
        return infoHeader("faction" to actualFaction.name)
    }
}