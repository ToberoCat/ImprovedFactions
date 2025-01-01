package io.github.toberocat.improvedfactions

import BuildConfig
import io.github.toberocat.improvedfactions.commands.executor.CommandExecutor
import io.github.toberocat.improvedfactions.modules.ModuleManager
import io.github.toberocat.improvedfactions.modules.base.BaseModule.plugin
import io.github.toberocat.improvedfactions.utils.CliPrinter
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin


/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
const val SPIGOT_RESOURCE_ID = 95617

open class ImprovedFactionsPlugin : JavaPlugin() {

    lateinit var moduleManager: ModuleManager
    lateinit var executor: CommandExecutor

    companion object {
        lateinit var instance: ImprovedFactionsPlugin
            private set
    }

    override fun onEnable() {
        val cliPrinter = CliPrinter(logger)
        cliPrinter.logHeading()
        saveDefaultConfig()
        instance = this
        moduleManager = ModuleManager(this)
        moduleManager.enableModules()
        executor = moduleManager.registerCommands()
    }

    override fun onDisable() {
        logger.info("ImprovedFactions v${BuildConfig.VERSION_NAME} build ${BuildConfig.BUILD_INCREMENT} by Tobero (Tobias Madlberger) is disabling.")
        moduleManager.disableModules()
    }

    fun registerListeners(vararg listeners: Listener) {
        listeners.forEach { plugin.server.pluginManager.registerEvents(it, plugin) }
    }
}