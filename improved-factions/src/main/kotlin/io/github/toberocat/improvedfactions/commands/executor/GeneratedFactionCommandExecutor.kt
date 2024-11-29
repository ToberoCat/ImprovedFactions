package io.github.toberocat.improvedfactions.commands.executor

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.general.VersionCommandProcessor
import io.github.toberocat.improvedfactions.commands.manage.CreateCommandProcessor
import io.github.toberocat.improvedfactions.commands.processor.getFactionCommandProcessors

class GeneratedFactionCommandExecutor(
    plugin: ImprovedFactionsPlugin,
) : CommandExecutor(plugin) {
    init {
        getFactionCommandProcessors(plugin, this).forEach { registerCommandProcessor(it) }
    }
}