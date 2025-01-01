package io.github.toberocat.improvedfactions.integrations.papi

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.OfflinePlayer

object PlaceholderIntegration {
    val placeholders = HashMap<String, (player: OfflinePlayer) -> String?>()

    init {
        ImprovedFactionsPlugin.instance.moduleManager.loadPapiPlaceholders(placeholders)
    }

    fun parsePlaceholder(player: OfflinePlayer?, params: String): String? {
        return player?.let { loggedTransaction { placeholders[params]?.invoke(it) } }
            ?: BaseModule.config.defaultPlaceholders[params]
    }
}