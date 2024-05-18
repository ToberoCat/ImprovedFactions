package io.github.toberocat.improvedfactions.modules.dynmap

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapModuleConfig
import io.github.toberocat.improvedfactions.modules.dynmap.handles.DummyFactionDynmapModuleHandles
import io.github.toberocat.improvedfactions.modules.dynmap.handles.FactionDynmapModuleHandle
import io.github.toberocat.improvedfactions.modules.dynmap.impl.FactionDynmapModuleHandleImpl
import io.github.toberocat.toberocore.command.CommandExecutor
import org.bukkit.Bukkit
import org.dynmap.DynmapCommonAPI
import org.dynmap.DynmapCommonAPIListener
import org.dynmap.markers.AreaMarker
import org.dynmap.markers.MarkerSet

class DynmapModule : BaseModule {

    override val moduleName = MODULE_NAME


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
            ImprovedFactionsPlugin.modules[MODULE_NAME] as? DynmapModule ?: throw IllegalStateException()

        fun dynmapPair() = MODULE_NAME to DynmapModule()
    }
}