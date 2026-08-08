package io.github.toberocat.improvedfactions.modules.power.handles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
interface FactionPowerRaidModuleHandle {
    fun reloadConfig(plugin: ImprovedFactionsPlugin)
    fun getPowerAccumulated(activeAccumulation: Double, inactiveAccumulation: Double): Double
}
