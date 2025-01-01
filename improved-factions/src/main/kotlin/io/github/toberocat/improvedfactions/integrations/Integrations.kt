package io.github.toberocat.improvedfactions.integrations

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.integrations.papi.PapiExpansion
import io.github.toberocat.improvedfactions.integrations.papi.PlaceholderIntegration
import io.github.toberocat.improvedfactions.integrations.papi.PlaceholderParser
import io.github.toberocat.improvedfactions.modules.base.BaseModule.logger
import org.bukkit.OfflinePlayer

class Integrations(private val plugin: ImprovedFactionsPlugin) {
    lateinit var placeholderParser: PlaceholderParser

    fun loadIntegrations() {
        placeholderParser = loadPlaceholderParser()
    }

    private fun loadPlaceholderParser(): PlaceholderParser {
        if (plugin.server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            logger.info("Loaded improved factions papi extension")
            return PapiExpansion().also { it.register() }
        }

        logger.info("Papi not found. Skipping Papi registration. Using internal placeholder parser.")
        return object : PlaceholderParser {
            override fun replacePlaceholders(player: OfflinePlayer, value: String): String {
                val regex = Regex("%faction_([a-zA-Z0-9_]+)%")
                return regex.replace(value) { matchResult ->
                    val key = matchResult.groupValues[1]
                    PlaceholderIntegration.parsePlaceholder(player, key) ?: matchResult.value
                }
            }
        }
    }
}