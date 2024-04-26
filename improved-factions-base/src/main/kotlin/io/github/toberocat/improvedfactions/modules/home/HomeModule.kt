package io.github.toberocat.improvedfactions.modules.home

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
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
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object HomeModule : BaseModule {
    const val MODULE_NAME = "home"
    override val moduleName = MODULE_NAME

    var homeModuleHandle: HomeModuleHandle = DummyHomeModuleHandle()

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        homeModuleHandle = HomeModuleHandleImpl()
    }

    override fun onLoadDatabase(plugin: ImprovedFactionsPlugin) {
        SchemaUtils.createMissingTablesAndColumns(FactionHomes)
    }

    override fun addCommands(plugin: ImprovedFactionsPlugin, executor: CommandExecutor) {
        executor.addChild(HomeSetCommand(plugin))
        executor.addChild(TeleportHomeCommand(plugin))
    }

    fun Faction.getHome() = homeModuleHandle.getHome(this@getHome)
    fun Faction.setHome(location: Location) = homeModuleHandle.setHome(this@setHome, location)
    fun Player.teleportToFactionHome() = transaction {
        factionUser().faction()?.let { faction ->
            homeModuleHandle.teleportToFactionHome(
                faction,
                this@teleportToFactionHome
            )
        }
    }

    fun FactionUser.teleportToFactionHome() = transaction {
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