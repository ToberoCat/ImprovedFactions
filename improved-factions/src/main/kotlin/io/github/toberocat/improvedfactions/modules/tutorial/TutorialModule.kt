package io.github.toberocat.improvedfactions.modules.tutorial

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.tutorial.command.TutorialCommand
import io.github.toberocat.improvedfactions.modules.tutorial.listener.PlayerListener
import io.github.toberocat.toberocore.command.CommandExecutor

object TutorialModule : BaseModule {
    const val MODULE_NAME = "tutorials"
    override val moduleName = MODULE_NAME
    override var isEnabled = false

    fun tutorialModulePair() = moduleName to this

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        plugin.registerListeners(
            PlayerListener()
        )
    }

    override fun addCommands(plugin: ImprovedFactionsPlugin, executor: CommandExecutor) {
        executor.addChild(TutorialCommand(plugin))
    }
}