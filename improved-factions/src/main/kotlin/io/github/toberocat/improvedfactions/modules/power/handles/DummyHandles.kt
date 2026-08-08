package io.github.toberocat.improvedfactions.modules.power.handles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
class DummyFactionPowerRaidModuleHandle : FactionPowerRaidModuleHandle {
    override fun reloadConfig(plugin: ImprovedFactionsPlugin) = Unit
    override fun getPowerAccumulated(activeAccumulation: Double, inactiveAccumulation: Double) = 0.0
}
