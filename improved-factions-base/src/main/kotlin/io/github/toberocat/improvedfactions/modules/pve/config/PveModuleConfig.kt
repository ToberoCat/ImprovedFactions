package io.github.toberocat.improvedfactions.modules.pve.config

import org.bukkit.configuration.file.FileConfiguration
import kotlin.math.abs

data class PveModuleConfig(
    var minRaidDistance: Int = 10,
    var maxRaidDistance: Int = 50,
    var minRaidSize: Int = 5,
    var maxRaidSize: Int = 10
) {
    private val configPath = "factions.pve"

    fun reload(config: FileConfiguration) {
        minRaidDistance = abs(config.getInt("$configPath.min-raid-distance", minRaidDistance))
        maxRaidDistance = abs(config.getInt("$configPath.max-raid-distance", maxRaidDistance))
        minRaidSize = abs(config.getInt("$configPath.min-raid-size", minRaidSize))
        maxRaidSize = abs(config.getInt("$configPath.max-raid-size", maxRaidSize))
    }

    fun raidDistance() = minRaidDistance..maxRaidDistance
}