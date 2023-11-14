package io.github.toberocat.improvedfactions.modules.base

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.toberocore.command.CommandExecutor

interface BaseModule {
    val moduleName: String;
    fun onEnable()
    fun reloadConfig(plugin: ImprovedFactionsPlugin)
    fun addCommands(plugin: ImprovedFactionsPlugin, executor: CommandExecutor)

    fun shouldEnable(plugin: ImprovedFactionsPlugin) = plugin.config.getBoolean("modules.$moduleName")
}