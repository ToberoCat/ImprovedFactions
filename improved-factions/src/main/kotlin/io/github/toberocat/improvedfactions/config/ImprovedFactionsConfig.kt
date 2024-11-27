package io.github.toberocat.improvedfactions.config

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.utils.FileUtils
import io.github.toberocat.improvedfactions.utils.getEnum
import org.bukkit.Bukkit
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption


class ImprovedFactionsConfig(
    var territoryDisplayLocation: EventDisplayLocation = EventDisplayLocation.TITLE,
    var defaultPlaceholders: Map<String, String> = emptyMap(),
    var allowedWorlds: Set<String> = emptySet(),
    var hideWildernessTitle: Boolean = false,
    var maxCommandSuggestions: Int = 10,
) : PluginConfig() {

    override fun reload(plugin: ImprovedFactionsPlugin, config: FileConfiguration) {
        territoryDisplayLocation = config.getEnum<EventDisplayLocation>("event-display-location")
            ?: EventDisplayLocation.TITLE
        defaultPlaceholders = config.generateDefaultPlaceholders()
        allowedWorlds = config.generateAllowedWorlds()
        maxCommandSuggestions = config.getInt("max-command-suggestions", maxCommandSuggestions)
        hideWildernessTitle = config.getBoolean("factions.hide-wilderness-title", hideWildernessTitle)
    }

    private fun FileConfiguration.generateDefaultPlaceholders() =
        getConfigurationSection("default-placeholders")?.getKeys(false)?.associateWith {
            getString("default-placeholders.$it") ?: ""
        } ?: emptyMap()

    companion object {
        fun createConfig(plugin: ImprovedFactionsPlugin): ImprovedFactionsConfig {
            val configFileVersion = plugin.config.getInt("config-version", 0)
            val latestConfig = getLatestConfigStream().use { YamlConfiguration.loadConfiguration(it.bufferedReader()) }
            val latestConfigVersion = latestConfig.getInt("config-version", 0)

            if (configFileVersion == latestConfigVersion) {
                plugin.logger.info("Config is up to date")
                return ImprovedFactionsConfig()
            }

            plugin.logger.info("Config is outdated $configFileVersion -> $latestConfigVersion. Updating...")
            plugin.logger.info("Backing up the old config...")

            val configBackups = File(plugin.dataFolder, "backups/configs")
            if (!configBackups.exists()) {
                configBackups.mkdirs()
            }

            Files.copy(
                File(plugin.dataFolder, "config.yml").toPath(),
                File(configBackups, "config-${configFileVersion}.yml").toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )

            val previousValueTree = generatePreviousValueTree(plugin)

            overrideOldConfig(plugin)
            plugin.reloadConfig()

            updateConfig(previousValueTree, plugin)
            plugin.saveConfig()

            plugin.logger.info("Config updated successfully")

            return ImprovedFactionsConfig()
        }

        fun generatePreviousValueTree(plugin: ImprovedFactionsPlugin) = plugin.config.getValues(true)
            .filter { it.value !is MemorySection }
            .filter { it.key != "config-version" }

        fun updateConfig(
            previousValueTree: Map<String, Any>,
            plugin: ImprovedFactionsPlugin
        ) {
            previousValueTree.forEach { plugin.config.set(it.key, it.value) }
            plugin.logger.info("${previousValueTree.size} values have been restored from the old config.")
        }

        private fun overrideOldConfig(plugin: ImprovedFactionsPlugin) {
            val destination = File(plugin.dataFolder, "config.yml")
            getLatestConfigStream().use { Files.copy(it, destination.toPath(), StandardCopyOption.REPLACE_EXISTING) }
        }

        private fun getLatestConfigStream() = this::class.java.getResourceAsStream("/config.yml")
            ?: throw IllegalStateException("Config not found")
    }
}