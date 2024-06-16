package io.github.toberocat.improvedfactions.modules.pve

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.pve.command.SpawnRaidCommand
import io.github.toberocat.improvedfactions.modules.pve.config.PveModuleConfig
import io.github.toberocat.improvedfactions.modules.pve.handle.DummyPveModuleHandle
import io.github.toberocat.improvedfactions.modules.pve.handle.PveModuleHandle
import io.github.toberocat.improvedfactions.modules.pve.impl.PveModuleHandleImpl
import io.github.toberocat.toberocore.command.CommandExecutor

class PveModule : BaseModule {
    override val moduleName = MODULE_NAME

    private val config = PveModuleConfig()
    private var handle: PveModuleHandle = DummyPveModuleHandle()

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        handle = PveModuleHandleImpl(plugin, config)
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        config.reload(plugin.config)
    }

    override fun addCommands(plugin: ImprovedFactionsPlugin, executor: CommandExecutor) {
        executor.addChild(SpawnRaidCommand(plugin, handle))
    }

    companion object {
        const val MODULE_NAME = "pve"

        fun pvePair() = MODULE_NAME to PveModule()
    }
}