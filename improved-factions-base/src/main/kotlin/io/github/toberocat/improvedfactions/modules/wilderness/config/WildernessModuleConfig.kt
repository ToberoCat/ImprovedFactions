package io.github.toberocat.improvedfactions.modules.wilderness.config

import org.bukkit.configuration.file.FileConfiguration
import java.util.concurrent.TimeUnit

class WildernessModuleConfig(
    var cooldown: Long = 30,
    var timeUnit: TimeUnit = TimeUnit.SECONDS,
) {

    fun reload(config: FileConfiguration) {
        cooldown = config.getLong("wilderness.cooldown", cooldown)
        timeUnit = TimeUnit.valueOf(config.getString("wilderness.time-unit", timeUnit.name)!!)
    }
}