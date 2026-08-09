package io.github.toberocat.improvedfactions.modules.home

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.processor.homeCommandProcessors
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.modules.home.commands.HomeSetCommand
import io.github.toberocat.improvedfactions.modules.home.commands.TeleportHomeCommand
import io.github.toberocat.toberocore.command.CommandExecutor
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import io.github.toberocat.improvedfactions.translation.sendLocalized

object HomeModule : Module {
    const val MODULE_NAME = "home"
    override val moduleName = MODULE_NAME
    override var isEnabled = false

    override fun getCommandProcessors(plugin: ImprovedFactionsPlugin) =
        homeCommandProcessors(plugin)

    enum class HomeTeleportStartResult { STARTED, UNREACHABLE, FAILED }

    fun Player.teleportToFactionHome(): HomeTeleportStartResult {
        val factionId = cachedUser().factionId
        val home = StorageManager.cache.home(factionId) ?: return HomeTeleportStartResult.FAILED
        val targetWorld = Bukkit.getWorld(home.world) ?: return HomeTeleportStartResult.FAILED
        val location = Location(targetWorld, home.x, home.y, home.z)
        if (!isHomeReachable(factionId, location)) {
            broadcastUnreachableHome(factionId)
            return HomeTeleportStartResult.UNREACHABLE
        }
        io.github.toberocat.improvedfactions.utils.PlayerTeleporter(
            ImprovedFactionsPlugin.instance,
            this,
            "home.messages.teleporting-title",
            "home.messages.teleporting-subtitle",
            {
                if (!isHomeReachable(factionId, location)) {
                    broadcastUnreachableHome(factionId)
                } else if (teleport(location)) {
                    sendLocalized("home.commands.home.teleport-home-success")
                } else {
                    sendLocalized("home.commands.home.teleport-home-failed")
                }
            },
            onCancelled = { sendLocalized("home.commands.home.teleport-cancelled") },
        ).startTeleport()
        return HomeTeleportStartResult.STARTED
    }

    private fun isHomeReachable(factionId: Int, location: Location) =
        StorageManager.cache.claim(location.claimKey())?.factionId == factionId

    private fun broadcastUnreachableHome(factionId: Int) {
        StorageManager.cache.factionMembers(factionId)
            .mapNotNull(Bukkit::getPlayer)
            .forEach { it.sendLocalized("home.commands.home.home-unreachable") }
    }

    fun homePair() = moduleName to this
}
