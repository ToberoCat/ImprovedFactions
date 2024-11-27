package io.github.toberocat.improvedfactions.commands.executor

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.TestCommandProcessor
import org.bukkit.command.PluginCommand

class GeneratedFactionCommandExecutor(
    plugin: ImprovedFactionsPlugin,
) : CommandExecutor(plugin) {
    init {
        registerCommandProcessor(TestCommandProcessor(plugin, this))
    }
}