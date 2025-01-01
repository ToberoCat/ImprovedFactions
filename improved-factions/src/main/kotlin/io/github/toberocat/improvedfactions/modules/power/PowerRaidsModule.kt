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
import io.github.toberocat.improvedfactions.utils.toCountdownTime
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

    @PapiPlaceholder("power", MODULE_NAME, "The power of your faction")
    @PapiPlaceholder("max_power", MODULE_NAME, "The maximum power of your faction")
    override fun onPapiPlaceholder(placeholders: HashMap<String, (player: OfflinePlayer) -> String?>) {
        placeholders["power"] = { it.factionUser().faction()?.accumulatedPower?.toString() }
        placeholders["max_power"] = { it.factionUser().faction()?.maxPower?.toString() }
        (powerModuleHandle as? FactionPowerRaidModuleHandleImpl)?.let {
            registerPowerSpecificPlaceholders(it, placeholders)
        }
    }

    fun powerRaidModule() = ImprovedFactionsPlugin.instance.moduleManager.getModule<PowerRaidsModule>(MODULE_NAME)
    fun powerRaidsPair() = MODULE_NAME to this


    @PapiPlaceholder("next_power_gain", MODULE_NAME, "The power that will be gained on the next power gain")
    @PapiPlaceholder("claim_upkeep_cost", MODULE_NAME, "The cost of maintaining your claims")
    @PapiPlaceholder("next_claim_cost", MODULE_NAME, "The cost of claiming the next chunk")
    @PapiPlaceholder(
        "active_accumulation",
        MODULE_NAME,
        "The power that is currently being accumulated by your active members"
    )
    @PapiPlaceholder(
        "inactive_accumulation",
        MODULE_NAME,
        "The power that you currently lose due to inactive members"
    )
    @PapiPlaceholder(
        "inactive_accumulation",
        MODULE_NAME,
        "The power that you currently lose due to inactive members"
    )
    @PapiPlaceholder(
        "next_accumulation_cycle",
        MODULE_NAME,
        "The time left until the next power accumulation cycle"
    )
    @PapiPlaceholder(
        "next_claim_keep_cost_cycle",
        MODULE_NAME,
        "The time left until the next claim keep cost cycle"
    )
    private fun registerPowerSpecificPlaceholders(
        handle: FactionPowerRaidModuleHandleImpl,
        placeholders: HashMap<String, (player: OfflinePlayer) -> String?>
    ) {
        placeholders["next_power_gain"] =
            { player -> player.factionUser().faction()?.let { handle.getPowerAccumulated(it).toString() } }
        placeholders["active_accumulation"] =
            { player -> player.factionUser().faction()?.let { handle.getActivePowerAccumulation(it).toString() } }
        placeholders["inactive_accumulation"] =
            { player -> player.factionUser().faction()?.let { handle.getInactivePowerAccumulation(it).toString() } }
        placeholders["claim_upkeep_cost"] =
            { player -> player.factionUser().faction()?.let { handle.getClaimMaintenanceCost(it).toString() } }
        placeholders["next_claim_cost"] =
            { player -> player.factionUser().faction()?.let { handle.getNextClaimCost(it).toString() } }
        placeholders["next_accumulation_cycle"] = {
            val nextCycleMs = handle.nextAccumulationCycleTime()
            (nextCycleMs - System.currentTimeMillis()).toCountdownTime()
        }
        placeholders["next_claim_keep_cost_cycle"] = {
            val nextCycleMs = handle.nextClaimKeepCostCycleTime()
            (nextCycleMs - System.currentTimeMillis()).toCountdownTime()
        }
    }
}