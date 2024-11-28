package io.github.toberocat.improvedfactions.commands.executor

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.manage.CreateCommandProcessor

class GeneratedFactionCommandExecutor(
    plugin: ImprovedFactionsPlugin,
) : CommandExecutor(plugin) {
    init {
        registerCommandProcessor(CreateCommandProcessor(plugin, this))
    }
}