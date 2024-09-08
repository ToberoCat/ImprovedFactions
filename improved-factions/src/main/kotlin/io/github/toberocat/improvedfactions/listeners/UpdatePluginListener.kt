package io.github.toberocat.improvedfactions.listeners

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
class UpdatePluginListener : Listener {
    init {
        Bukkit.getOnlinePlayers().forEach { send(it) }
    }

    @EventHandler
    private fun join(event: PlayerJoinEvent) = send(event.player)

    private fun send(player: Player) {
        if (!player.isOp) return
        player.sendMessage("§bA newer version of §eImprovedFactions§b is available for you. Check it out on spigotmc.org")
    }

}
