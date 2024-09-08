package io.github.toberocat.improvedfactions.modules.chat.config

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.config.PluginConfig
import org.bukkit.configuration.file.FileConfiguration

class ChatModuleConfig(
    var logFactionChatsToConsole: Boolean = true,
) : PluginConfig() {

    private val configPath = "factions.chat"

    override fun reload(plugin: ImprovedFactionsPlugin, config: FileConfiguration) {
        logFactionChatsToConsole = config.getBoolean("$configPath.log-faction-chats-to-console", logFactionChatsToConsole)
    }
}