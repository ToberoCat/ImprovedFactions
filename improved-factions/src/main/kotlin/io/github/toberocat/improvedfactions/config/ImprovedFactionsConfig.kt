package io.github.toberocat.improvedfactions.config

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.base.BaseModule.config
import io.github.toberocat.improvedfactions.utils.getEnum
import io.github.toberocat.improvedfactions.zone.ZoneHandler
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
    var mapWidth: Int = 20,
    var mapHeight: Int = 10,
    var maxNameLength: Int = 36,
    var factionNameRegex: Regex = Regex("[a-zA-Z]*"),
    var maxFactionIconLength: Int = 5000,
    var inviteExpiresInMinutes: Int = 5,
    var maxRankNameLength: Int = 50,
    var rankNameRegex: Regex = Regex("[a-zA-Z]*"),
    var guestRankName: String = "Guest",
    var hideDecorativeParticles: Boolean = false,
    var particleTickSpeed: Long = 1,
    var maxClaimRadius: Int = 10,
) : PluginConfig() {

    override fun reload(plugin: ImprovedFactionsPlugin, config: FileConfiguration) {
        territoryDisplayLocation = config.getEnum<EventDisplayLocation>("event-display-location")
            ?: territoryDisplayLocation
        defaultPlaceholders = config.generateDefaultPlaceholders()
        allowedWorlds = config.generateAllowedWorlds()
        maxCommandSuggestions = config.getInt("max-command-suggestions", maxCommandSuggestions)
        hideWildernessTitle = config.getBoolean("factions.hide-wilderness-title", hideWildernessTitle)
        mapWidth = config.getInt("factions.map-width", mapWidth)
        mapHeight = config.getInt("factions.map-height", mapHeight)
        maxNameLength = config.getInt("factions.unsafe.max-name-length", maxNameLength)
        maxFactionIconLength = config.getInt("factions.unsafe.max-icon-length", maxFactionIconLength)
        factionNameRegex = Regex(config.getString("factions.name-regex") ?: "[a-zA-Z]*")
        inviteExpiresInMinutes = config.getInt("factions.invites-expire-in", inviteExpiresInMinutes)
        maxRankNameLength = config.getInt("factions.max-rank-name-length", maxRankNameLength)
        rankNameRegex = Regex(config.getString("factions.rank-name-regex") ?: "[a-zA-Z ]*")
        guestRankName = config.getString("factions.unsafe.guest-rank-name") ?: guestRankName
        hideDecorativeParticles = config.getBoolean("performance.decorative-particles.hidden", hideDecorativeParticles)
        particleTickSpeed = config.getLong("performance.decorative-particles.tick-speed", particleTickSpeed)
        maxClaimRadius = config.getInt("factions.max-claim-radius", maxClaimRadius)

        getZones(plugin, config)
    }

    private fun getZones(plugin: ImprovedFactionsPlugin, config: FileConfiguration) {
        config.getConfigurationSection("zones")?.getKeys(false)?.let {
            it.forEach { zone ->
                val section = config.getConfigurationSection("zones.$zone")
                if (section == null) {
                    BaseModule.logger.warning("Invalid formatted zone $zone found in config")
                    return@forEach
                }
                ZoneHandler.createZone(plugin, zone, section)
            }
        }

        ZoneHandler.defaultZoneCheck(plugin)
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
            plugin: ImprovedFactionsPlugin,
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