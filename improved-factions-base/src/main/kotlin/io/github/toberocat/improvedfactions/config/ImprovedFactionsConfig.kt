package io.github.toberocat.improvedfactions.config

import io.github.toberocat.improvedfactions.utils.getEnum
import org.bukkit.configuration.file.FileConfiguration

class ImprovedFactionsConfig(config: FileConfiguration) {
    val territoryDisplayLocation = config.getEnum<EventDisplayLocation>("event-display-location")
        ?: EventDisplayLocation.TITLE
    val defaultPlaceholders: Map<String, String> = generateDefaultPlaceholders(config)

    private fun generateDefaultPlaceholders(config: FileConfiguration): Map<String, String> {
        return config.getConfigurationSection("default-placeholders")?.getKeys(false)?.associateWith {
            config.getString("default-placeholders.$it") ?: ""
        } ?: emptyMap()
    }
}