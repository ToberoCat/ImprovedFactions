package io.github.toberocat.improvedfactions.config

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration

abstract class PluginConfig {
    protected val logger = ImprovedFactionsPlugin.instance.logger

    abstract fun reload(plugin: ImprovedFactionsPlugin, config: FileConfiguration)

    protected fun FileConfiguration.generateAllowedWorlds() = computeAllowedWorlds(
        getStringList("whitelisted-worlds").toSet(),
        getStringList("blacklisted-worlds").toSet()
    )

    protected fun computeAllowedWorlds(whitelist: Set<String>, blacklist: Set<String>) = when (whitelist.isEmpty()) {
        true -> Bukkit.getWorlds().map { it.name }.toSet()
        false -> whitelist
    }.subtract(blacklist)
}