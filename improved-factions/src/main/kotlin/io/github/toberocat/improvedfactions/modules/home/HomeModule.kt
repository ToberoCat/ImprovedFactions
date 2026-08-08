package io.github.toberocat.improvedfactions.modules.home

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.processor.homeCommandProcessors
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.modules.home.commands.HomeSetCommand
import io.github.toberocat.improvedfactions.modules.home.commands.TeleportHomeCommand
import io.github.toberocat.toberocore.command.CommandExecutor
import org.bukkit.Location
import org.bukkit.entity.Player

object HomeModule : Module {
    const val MODULE_NAME = "home"
    override val moduleName = MODULE_NAME
    override var isEnabled = false

    override fun getCommandProcessors(plugin: ImprovedFactionsPlugin) =
        homeCommandProcessors(plugin)

    fun Player.teleportToFactionHome(): Boolean {
        val factionId = cachedUser().factionId
        val home = StorageManager.cache.home(factionId) ?: return false
        val targetWorld = org.bukkit.Bukkit.getWorld(home.world) ?: return false
        val location = Location(targetWorld, home.x, home.y, home.z)
        io.github.toberocat.improvedfactions.utils.PlayerTeleporter(
            ImprovedFactionsPlugin.instance,
            this,
            "home.messages.teleporting-title",
            "home.messages.teleporting-subtitle",
            { teleport(location) }
        ).startTeleport()
        return true
    }

    fun homePair() = moduleName to this
}
