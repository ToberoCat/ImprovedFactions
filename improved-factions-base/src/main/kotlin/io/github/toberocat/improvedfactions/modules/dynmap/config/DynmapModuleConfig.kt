package io.github.toberocat.improvedfactions.modules.dynmap.config

import org.bukkit.configuration.file.FileConfiguration

data class DynmapModuleConfig(
    var infoWindows: Map<String, String> = emptyMap(),
    var allowFactionInfoWindowCustomization: Boolean = false,
    var markerSetId: String = "factions",
    var markerSetDisplayName: String = "Factions",
    var markerSetPriority: Int = 10,
    var markerSetHiddenByDefault: Boolean = false,
    var showZones: Boolean = false
) {
    private val configPath = "factions.dynmap"

    fun reload(config: FileConfiguration) {
        allowFactionInfoWindowCustomization = config.getBoolean("$configPath.allow-faction-info-window-customization", allowFactionInfoWindowCustomization)
        markerSetId = config.getString("$configPath.marker-set.id") ?: markerSetId
        markerSetDisplayName = config.getString("$configPath.marker-set.display-name") ?: markerSetDisplayName
        markerSetPriority = config.getInt("$configPath.marker-set.layer-priority", markerSetPriority)
        markerSetHiddenByDefault = config.getBoolean("$configPath.marker-set.hidden-by-default", markerSetHiddenByDefault)
        showZones = config.getBoolean("$configPath.show-zones", showZones)
        config.getConfigurationSection("$configPath.info-windows")?.let { section ->
            infoWindows = section.getKeys(false).associateWith { section.getString(it)!! }
        }
    }
}