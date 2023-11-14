package io.github.toberocat.improvedfactions.modules.power

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.power.commands.PowerCommand
import io.github.toberocat.improvedfactions.modules.power.handles.DummyFactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.toberocore.command.CommandExecutor

class PowerRaidsModule : BaseModule {
    override val moduleName = MODULE_NAME;

    var factionModuleHandle: FactionPowerRaidModuleHandle = DummyFactionPowerRaidModuleHandle()

    override fun onEnable() {
        factionModuleHandle = FactionPowerRaidModuleHandleImpl(this)
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        factionModuleHandle.reloadConfig(plugin)
    }

    override fun addCommands(plugin: ImprovedFactionsPlugin, executor: CommandExecutor) {
        executor.addChild(PowerCommand(plugin))
    }

    companion object {
        const val MODULE_NAME = "power-raids"
        fun powerRaidModule() =
            (ImprovedFactionsPlugin.modules[MODULE_NAME] as? PowerRaidsModule) ?: throw IllegalStateException()
        fun powerRaidsPair() = MODULE_NAME to PowerRaidsModule()
    }
}