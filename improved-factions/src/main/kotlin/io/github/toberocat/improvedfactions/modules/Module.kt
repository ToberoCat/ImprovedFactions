package io.github.toberocat.improvedfactions.modules

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.toberocore.command.CommandExecutor
import org.bukkit.OfflinePlayer

interface Module {
    val moduleName: String
    var isEnabled: Boolean

    fun onEnable(plugin: ImprovedFactionsPlugin) {}
    fun onEverythingEnabled(plugin: ImprovedFactionsPlugin) {}
    fun onDisable(plugin: ImprovedFactionsPlugin) {}
    fun reloadConfig(plugin: ImprovedFactionsPlugin) {}
    fun addCommands(plugin: ImprovedFactionsPlugin, executor: CommandExecutor) {}
    fun onLoadDatabase(plugin: ImprovedFactionsPlugin) {}
    fun onPapiPlaceholder(placeholders: HashMap<String, (player: OfflinePlayer) -> String?>) {}

    fun shouldEnable(plugin: ImprovedFactionsPlugin) = plugin.config.getBoolean("modules.$moduleName")

    fun warn(message: String) {
        ImprovedFactionsPlugin.instance.logger.info("[$moduleName] $message")
    }
}