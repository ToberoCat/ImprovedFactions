package io.github.toberocat.improvedfactions.modules.wilderness

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.processor.wildernessCommandProcessors
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.modules.wilderness.commands.WildernessCommand
import io.github.toberocat.improvedfactions.modules.wilderness.config.WildernessModuleConfig

object WildernessModule : Module {
    const val MODULE_NAME = "wilderness"
    override val moduleName = MODULE_NAME
    override var isEnabled = true

    val config = WildernessModuleConfig()

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        config.reload(plugin, plugin.config)
    }

    override fun getCommandProcessors(plugin: ImprovedFactionsPlugin) = wildernessCommandProcessors(plugin)


    fun wildernessPair() = MODULE_NAME to this
}