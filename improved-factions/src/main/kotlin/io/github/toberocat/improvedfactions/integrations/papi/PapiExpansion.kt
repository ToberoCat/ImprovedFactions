package io.github.toberocat.improvedfactions.integrations.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import me.clip.placeholderapi.PlaceholderAPI


/**
 * Created: 20.07.2023
 * @author Tobias Madlberger (Tobias)
 */

class PapiExpansion : PlaceholderExpansion(), PlaceholderParser {
    override fun getAuthor(): String = "Tobero"

    override fun getIdentifier(): String = "faction"

    override fun getVersion(): String = "1.0.0"

    override fun persist(): Boolean = true

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        return PlaceholderIntegration.parsePlaceholder(player, params)
    }

    override fun replacePlaceholders(player: OfflinePlayer, value: String): String {
        return PlaceholderAPI.setPlaceholders(player, value)
    }
}
