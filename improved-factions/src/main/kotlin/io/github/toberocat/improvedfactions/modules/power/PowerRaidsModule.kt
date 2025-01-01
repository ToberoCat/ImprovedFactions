package io.github.toberocat.improvedfactions.modules.power

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.annotations.papi.PapiPlaceholder
import io.github.toberocat.improvedfactions.commands.processor.powerRaidsCommandProcessors
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.modules.power.config.PowerManagementConfig
import io.github.toberocat.improvedfactions.modules.power.handles.DummyFactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.modules.power.listener.PlayerDeathListener
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.OfflinePlayer

object PowerRaidsModule : Module {
    const val MODULE_NAME = "power-raids"
    override val moduleName = MODULE_NAME
    override var isEnabled = false

    var powerModuleHandle: FactionPowerRaidModuleHandle = DummyFactionPowerRaidModuleHandle()
    val config = PowerManagementConfig()

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        val module = FactionPowerRaidModuleHandleImpl(config)
        powerModuleHandle = module

        plugin.server.pluginManager.registerEvents(PlayerDeathListener(module), plugin)
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        config.reload(plugin.config)
        powerModuleHandle.reloadConfig(plugin)
    }

    override fun getCommandProcessors(plugin: ImprovedFactionsPlugin) =
        powerRaidsCommandProcessors(plugin)

    override fun onPapiPlaceholder(placeholders: HashMap<String, (player: OfflinePlayer) -> String?>) {
        placeholders["power"] = { it.factionUser().faction()?.accumulatedPower?.toString() }
        placeholders["maxPower"] = { it.factionUser().faction()?.maxPower?.toString() }
        (powerModuleHandle as? FactionPowerRaidModuleHandleImpl)?.let {
            registerPowerSpecificPlaceholders(it, placeholders)
        }
    }

    fun powerRaidModule() = ImprovedFactionsPlugin.instance.moduleManager.getModule<PowerRaidsModule>(MODULE_NAME)
    fun powerRaidsPair() = MODULE_NAME to this


    private fun registerPowerSpecificPlaceholders(
        handle: FactionPowerRaidModuleHandleImpl,
        placeholders: HashMap<String, (player: OfflinePlayer) -> String?>
    ) {
        @PapiPlaceholder("active_accumulation")
        placeholders["active_accumulation"] =
            { player -> player.factionUser().faction()?.let { handle.getActivePowerAccumulation(it).toString() } }

        @PapiPlaceholder("inactive_accumulation")
        placeholders["inactive_accumulation"] =
            { player -> player.factionUser().faction()?.let { handle.getInactivePowerAccumulation(it).toString() } }

        @PapiPlaceholder("claim_upkeep_cost")
        placeholders["claim_upkeep_cost"] =
            { player -> player.factionUser().faction()?.let { handle.getClaimMaintenanceCost(it).toString() } }

        @PapiPlaceholder("next_claim_cost")
        placeholders["next_claim_cost"] =
            { player -> player.factionUser().faction()?.let { handle.getNextClaimCost(it).toString() } }
    }
}