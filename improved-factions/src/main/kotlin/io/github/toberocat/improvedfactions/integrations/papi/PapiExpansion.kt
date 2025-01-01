package io.github.toberocat.improvedfactions.integrations.papi

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.config.ImprovedFactionsConfig
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.toOfflinePlayer
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction 


/**
 * Created: 20.07.2023
 * @author Tobias Madlberger (Tobias)
 */

class PapiExpansion(private val pluginConfig: ImprovedFactionsConfig) : PlaceholderExpansion() {
    private val placeholders = HashMap<String, (player: OfflinePlayer) -> String?>()

    init {
        ImprovedFactionsPlugin.instance.moduleManager.loadPapiPlaceholders(placeholders)
    }

    override fun getAuthor(): String = "Tobero"

    override fun getIdentifier(): String = "faction"

    override fun getVersion(): String = "1.0.0"

    override fun persist(): Boolean = true

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        return player?.let { loggedTransaction { placeholders[params]?.invoke(it) } }
            ?: pluginConfig.defaultPlaceholders[params]
    }
}
