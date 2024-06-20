package io.github.toberocat.improvedfactions.modules.webmap

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.webmap.config.DynmapModuleConfig
import io.github.toberocat.improvedfactions.modules.webmap.handles.DummyFactionWebMapModuleHandles
import io.github.toberocat.improvedfactions.modules.webmap.handles.FactionWebMapModuleHandle
import io.github.toberocat.improvedfactions.modules.webmap.impl.FactionDynmapModuleHandleImpl
import org.bukkit.Bukkit
import org.dynmap.DynmapCommonAPI
import org.dynmap.DynmapCommonAPIListener

class WebMapModule : BaseModule {

    override val moduleName = MODULE_NAME

    var webMapModuleHandle: FactionWebMapModuleHandle = DummyFactionWebMapModuleHandles()

    val config = DynmapModuleConfig()

    private val supportedWebMaps = setOf("dynmap", "pl3xmap")

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        supportedWebMaps.map { it to Bukkit.getPluginManager().isPluginEnabled(it) }
            .firstOrNull { it.second }
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        config.reload(plugin.config)
    }

    override fun shouldEnable(plugin: ImprovedFactionsPlugin): Boolean {
        val shouldEnable = super.shouldEnable(plugin)
        if (!shouldEnable) {
            return false
        }

        if (supportedWebMaps.any { Bukkit.getPluginManager().isPluginEnabled(it) }) {
            return true
        }

        warn(
            "Webmap module is enabled but no supported webmap is installed. Disabling Webmap module. ${
                supportedWebMaps.joinToString(
                    ", ",
                    "Supported webmaps: ",
                    "."
                )
            }"
        )
        return false
    }

    companion object {
        const val MODULE_NAME = "webmap"
        fun dynmapModule() =
            ImprovedFactionsPlugin.instance.moduleManager.getModule<WebMapModule>(MODULE_NAME)

        fun dynmapPair() = MODULE_NAME to WebMapModule()
    }
}