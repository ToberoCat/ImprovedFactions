package io.github.toberocat.improvedfactions.integrations

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.integrations.papi.PapiExpansion
import io.github.toberocat.improvedfactions.integrations.papi.PapiIntegration
import io.github.toberocat.improvedfactions.modules.base.BaseModule.config
import io.github.toberocat.improvedfactions.modules.base.BaseModule.logger
import org.bukkit.OfflinePlayer

class Integrations(private val plugin: ImprovedFactionsPlugin) {
    lateinit var papiIntegration: PapiIntegration

    fun loadIntegrations() {
        papiIntegration = loadPapiIntegration()
    }

    private fun loadPapiIntegration(): PapiIntegration {
        if (plugin.server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            PapiExpansion(config).register()
            logger.info("Loaded improved factions papi extension")
            return PapiIntegration.create()
        }

        logger.info("Papi not found. Skipping Papi registration")
        return object : PapiIntegration {
            override fun replacePlaceholders(player: OfflinePlayer, value: String) = value
        }
    }
}