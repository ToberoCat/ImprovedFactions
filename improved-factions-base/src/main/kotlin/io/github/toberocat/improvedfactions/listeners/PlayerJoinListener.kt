package io.github.toberocat.improvedfactions.listeners

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.utils.offline.KnownOfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener : Listener {

    @EventHandler
    private fun onJoin(event: PlayerJoinEvent) {
        val player: Player = event.player

        loggedTransaction {
            val knownOfflinePlayer = KnownOfflinePlayer.findById(player.uniqueId)
            if (knownOfflinePlayer != null) {
                knownOfflinePlayer.name = player.name
                return@loggedTransaction
            }

            ImprovedFactionsPlugin.instance.logger.info("[OfflinePlayers] Created new offline data " + player.uniqueId)

            KnownOfflinePlayer.new(player.uniqueId) {
                name = player.name
            }
        }
    }
}