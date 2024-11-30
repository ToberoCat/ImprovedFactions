package io.github.toberocat.improvedfactions.modules

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin.Companion.instance
import io.github.toberocat.improvedfactions.commands.executor.CommandExecutor
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.chat.ChatModule
import io.github.toberocat.improvedfactions.modules.claimparticle.ClaimParticleModule
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.modules.home.HomeModule
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.relations.RelationsModule
import io.github.toberocat.improvedfactions.modules.wilderness.WildernessModule
import org.bukkit.OfflinePlayer

class ModuleManager(private val plugin: ImprovedFactionsPlugin) {
    val activeModules: Map<String, Module>
    val modules = mutableMapOf(
        BaseModule.basePair(),
        PowerRaidsModule.powerRaidsPair(),
        DynmapModule.dynmapPair(),
        WildernessModule.wildernessPair(),
        HomeModule.homePair(),
        ChatModule.chatPair(),
        ClaimParticleModule.claimParticlesPair(),
        RelationsModule.relationsModulePair()
    )

    init {
        activeModules = modules.filter { it.value.shouldEnable(instance) }
    }

    fun reloadModuleConfigs() = modules.values.forEach { it.reloadConfig(plugin) }

    fun enableModules() {
        activeModules.forEach { (name, module) ->
            module.onEnable(plugin)
            module.isEnabled = true
            plugin.logger.info("Loaded module $name")
            module.reloadConfig(plugin)
            module.onLoadDatabase(plugin)
        }

        activeModules.forEach { (_, module) -> module.onEverythingEnabled(plugin) }
    }

    fun registerCommands() {
        val processors = activeModules.flatMap { it.value.getCommandProcessors(plugin) }
        val executor = CommandExecutor(plugin)
        executor.bindToPluginCommand("factions")
        processors.forEach { executor.registerCommandProcessor(it) }
    }

    fun disableModules() {
        activeModules.forEach { (_, module) ->
            module.onDisable(plugin)
            module.isEnabled = false
        }
    }


    fun loadPapiPlaceholders(placeholders: HashMap<String, (player: OfflinePlayer) -> String?>) =
        activeModules.forEach { (_, module) -> module.onPapiPlaceholder(placeholders) }

    inline fun <reified T> getModule(moduleName: String) = modules[moduleName] as? T ?: throw IllegalStateException()
}