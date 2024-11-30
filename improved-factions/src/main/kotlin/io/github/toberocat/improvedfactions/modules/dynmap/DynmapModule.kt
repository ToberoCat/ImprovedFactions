package io.github.toberocat.improvedfactions.modules.dynmap

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapModuleConfig
import io.github.toberocat.improvedfactions.modules.dynmap.handles.DummyFactionDynmapModuleHandles
import io.github.toberocat.improvedfactions.modules.dynmap.handles.FactionDynmapModuleHandle
import io.github.toberocat.improvedfactions.modules.dynmap.impl.FactionDynmapModuleHandleImpl
import org.bukkit.Bukkit
import org.dynmap.DynmapCommonAPI
import org.dynmap.DynmapCommonAPIListener

class DynmapModule : Module {
    override val moduleName = MODULE_NAME
    override var isEnabled = false


    var dynmapModuleHandle: FactionDynmapModuleHandle = DummyFactionDynmapModuleHandles()

    val config = DynmapModuleConfig()

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        DynmapCommonAPIListener.register(
            object : DynmapCommonAPIListener() {
                override fun apiEnabled(api: DynmapCommonAPI) {
                    dynmapModuleHandle = FactionDynmapModuleHandleImpl(config, plugin, api)
                }
            }
        )
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        config.reload(plugin.config)
    }

    override fun shouldEnable(plugin: ImprovedFactionsPlugin): Boolean {
        val shouldEnable = super.shouldEnable(plugin)
        if (!shouldEnable) {
            return false
        }

        if (Bukkit.getPluginManager().isPluginEnabled("dynmap")) {
            return true
        }

        warn("Dynmap module is enabled but Dynmap is not installed. Disabling Dynmap module.")
        return false
    }

    companion object {
        const val MODULE_NAME = "dynmap"
        fun dynmapModule() =
            ImprovedFactionsPlugin.instance.moduleManager.getModule<DynmapModule>(MODULE_NAME)

        fun dynmapPair() = MODULE_NAME to DynmapModule()
    }
}