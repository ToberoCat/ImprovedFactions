package io.github.toberocat.improvedfactions.utils

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.charts.addFactionsChart
import io.github.toberocat.improvedfactions.charts.addModuleChart
import org.bstats.bukkit.Metrics

/**
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
class BStatsCollector(plugin: ImprovedFactionsPlugin) {
    init {
        val metrics = Metrics(plugin, 14810)
        metrics.addModuleChart()
        metrics.addFactionsChart()
    }
}
