package io.github.toberocat.improvedfactions.config

import io.github.toberocat.improvedfactions.utils.getEnum
import org.bukkit.configuration.file.FileConfiguration

class ImprovedFactionsConfig(config: FileConfiguration) {
    val territoryDisplayLocation = config.getEnum<EventDisplayLocation>("territory-display-location")
        ?: EventDisplayLocation.TITLE
}