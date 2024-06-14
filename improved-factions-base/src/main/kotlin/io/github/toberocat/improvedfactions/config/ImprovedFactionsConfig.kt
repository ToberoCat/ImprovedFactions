package io.github.toberocat.improvedfactions.config

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.utils.FileUtils
import io.github.toberocat.improvedfactions.utils.getEnum
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption


class ImprovedFactionsConfig(config: FileConfiguration) {
    val territoryDisplayLocation = config.getEnum<EventDisplayLocation>("event-display-location")
        ?: EventDisplayLocation.TITLE
    val defaultPlaceholders: Map<String, String> = generateDefaultPlaceholders(config)

    private fun generateDefaultPlaceholders(config: FileConfiguration): Map<String, String> {
        return config.getConfigurationSection("default-placeholders")?.getKeys(false)?.associateWith {
            config.getString("default-placeholders.$it") ?: ""
        } ?: emptyMap()
    }

    companion object {
        fun createConfig(plugin: ImprovedFactionsPlugin): ImprovedFactionsConfig {
            val configFileVersion = plugin.config.getInt("config-version", 0)
            val latestConfig = getLatestConfigStream().use { YamlConfiguration.loadConfiguration(it.bufferedReader()) }
            val latestConfigVersion = latestConfig.getInt("config-version", 0)

            if (configFileVersion == latestConfigVersion) {
                plugin.logger.info("Config is up to date")
                return ImprovedFactionsConfig(plugin.config)
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

            return ImprovedFactionsConfig(plugin.config)
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