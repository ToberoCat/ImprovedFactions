package io.github.toberocat.improvedfactions.integrations.papi

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer

interface PapiIntegration {
    companion object {
        fun create() = object : PapiIntegration {
            override fun replacePlaceholders(player: OfflinePlayer, value: String) =
                PlaceholderAPI.setPlaceholders(player, value)
        }
    }

    fun replacePlaceholders(player: OfflinePlayer, value: String): String
}