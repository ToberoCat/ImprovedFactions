package io.github.toberocat.improvedfactions.integrations.papi

import org.bukkit.OfflinePlayer

interface PlaceholderParser {
    fun replacePlaceholders(player: OfflinePlayer, value: String): String
}