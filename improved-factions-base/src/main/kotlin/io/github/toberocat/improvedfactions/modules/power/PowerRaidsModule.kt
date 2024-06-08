package io.github.toberocat.improvedfactions.modules.power

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.power.commands.PowerCommand
import io.github.toberocat.improvedfactions.modules.power.config.PowerManagementConfig
import io.github.toberocat.improvedfactions.modules.power.handles.DummyFactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.modules.power.listener.PlayerDeathListener
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.toberocore.command.CommandExecutor
import org.bukkit.OfflinePlayer

class PowerRaidsModule : BaseModule {
    override val moduleName = MODULE_NAME

    var isEnabled = false
    var powerModuleHandle: FactionPowerRaidModuleHandle = DummyFactionPowerRaidModuleHandle()
    val config = PowerManagementConfig()

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        isEnabled = true

        val module = FactionPowerRaidModuleHandleImpl(config)
        powerModuleHandle = module

        plugin.server.pluginManager.registerEvents(PlayerDeathListener(module), plugin)
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        config.reload(plugin.config)
        powerModuleHandle.reloadConfig(plugin)
    }

    override fun addCommands(plugin: ImprovedFactionsPlugin, executor: CommandExecutor) {
        executor.addChild(PowerCommand(plugin, powerModuleHandle as FactionPowerRaidModuleHandleImpl))
    }

    override fun onPapiPlaceholder(placeholders: HashMap<String, (player: OfflinePlayer) -> String?>) {
        placeholders["power"] = { it.factionUser().faction()?.accumulatedPower?.toString() }
        placeholders["maxPower"] = { it.factionUser().faction()?.maxPower?.toString() }
        (powerModuleHandle as? FactionPowerRaidModuleHandleImpl)?.let { handle ->
            placeholders["active_accumulation"] =
                { player -> player.factionUser().faction()?.let { handle.getActivePowerAccumulation(it).toString() } }
            placeholders["inactive_accumulation"] =
                { player -> player.factionUser().faction()?.let { handle.getInactivePowerAccumulation(it).toString() } }
            placeholders["claim_upkeep_cost"] =
                { player -> player.factionUser().faction()?.let { handle.getClaimMaintenanceCost(it).toString() } }
        }
    }

    companion object {
        const val MODULE_NAME = "power-raids"
        fun powerRaidModule() = ImprovedFactionsPlugin.instance.moduleManager.getModule<PowerRaidsModule>(MODULE_NAME)
        fun powerRaidsPair() = MODULE_NAME to PowerRaidsModule()
    }
}