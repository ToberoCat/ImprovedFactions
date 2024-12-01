package io.github.toberocat.improvedfactions.modules.home

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.processor.homeCommandProcessors
import io.github.toberocat.improvedfactions.database.DatabaseManager
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.modules.home.commands.HomeSetCommand
import io.github.toberocat.improvedfactions.modules.home.commands.TeleportHomeCommand
import io.github.toberocat.improvedfactions.modules.home.data.FactionHomes
import io.github.toberocat.improvedfactions.modules.home.handles.DummyHomeModuleHandle
import io.github.toberocat.improvedfactions.modules.home.handles.HomeModuleHandle
import io.github.toberocat.improvedfactions.modules.home.impl.HomeModuleHandleImpl
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.toberocore.command.CommandExecutor
import org.bukkit.Location
import org.bukkit.entity.Player

object HomeModule : Module {
    const val MODULE_NAME = "home"
    override val moduleName = MODULE_NAME
    override var isEnabled = false

    private var homeModuleHandle: HomeModuleHandle = DummyHomeModuleHandle()

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        homeModuleHandle = HomeModuleHandleImpl()
    }

    override fun onLoadDatabase(plugin: ImprovedFactionsPlugin) {
        loggedTransaction { DatabaseManager.createTables(FactionHomes) }
    }

    override fun getCommandProcessors(plugin: ImprovedFactionsPlugin) =
        homeCommandProcessors(plugin)

    fun Faction.getHome() = homeModuleHandle.getHome(this@getHome)
    fun Faction.setHome(location: Location) = homeModuleHandle.setHome(this@setHome, location)
    fun Player.teleportToFactionHome() = loggedTransaction {
        factionUser().faction()?.let { faction ->
            homeModuleHandle.teleportToFactionHome(
                faction,
                this@teleportToFactionHome
            )
        }
    }

    fun FactionUser.teleportToFactionHome() = loggedTransaction {
        faction()?.let { faction ->
            player()?.let { player ->
                homeModuleHandle.teleportToFactionHome(
                    faction,
                    player
                )
            } ?: false
        }
    }

    fun homePair() = moduleName to this
}