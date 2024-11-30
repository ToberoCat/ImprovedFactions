package io.github.toberocat.improvedfactions.modules.wilderness

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.modules.wilderness.commands.WildernessCommand
import io.github.toberocat.improvedfactions.modules.wilderness.config.WildernessModuleConfig
import io.github.toberocat.toberocore.command.CommandExecutor

class WildernessModule : Module {
    override val moduleName = MODULE_NAME
    override var isEnabled = true
    private val wildernessConfig = WildernessModuleConfig()

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        wildernessConfig.reload(plugin, plugin.config)
    }

    override fun addCommands(plugin: ImprovedFactionsPlugin, executor: CommandExecutor) {
        executor.addChild(WildernessCommand(plugin, wildernessConfig))
    }

    companion object {
        const val MODULE_NAME = "wilderness"

        fun wildernessPair() = MODULE_NAME to WildernessModule()
    }
}