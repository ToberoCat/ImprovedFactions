package io.github.toberocat.improvedfactions.factions

import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

fun requiredDefaultFactionRanks(config: FileConfiguration): ConfigurationSection =
    requireNotNull(config.getConfigurationSection("factions.default-faction-ranks")) {
        "Missing required config section: factions.default-faction-ranks"
    }.also { configuredRanks ->
        require(configuredRanks.getKeys(false).isNotEmpty()) {
            "Config section factions.default-faction-ranks must define at least one rank"
        }
    }

fun defaultFactionRanks(configuredRanks: ConfigurationSection): List<GameStateCommands.DefaultRankSpec> =
    configuredRanks.getKeys(false).map { rankName ->
        GameStateCommands.DefaultRankSpec(
            rankName,
            configuredRanks.getInt("$rankName.priority"),
            configuredRanks.getStringList("$rankName.default-permissions").toSet()
        )
    }
