package io.github.toberocat.improvedfactions.charts

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.FactionHandler
import org.bstats.bukkit.Metrics
import org.bstats.charts.AdvancedPie
import org.bstats.charts.SingleLineChart

fun Metrics.addModuleChart() {
    addCustomChart(AdvancedPie("modules") {
        ImprovedFactionsPlugin.getActiveModules().map { it.key to 1 }.toMap()
    })
}

fun Metrics.addFactionsChart() {
    addCustomChart(SingleLineChart("factions") {
        FactionHandler.getFactions().count().toInt()
    })
}