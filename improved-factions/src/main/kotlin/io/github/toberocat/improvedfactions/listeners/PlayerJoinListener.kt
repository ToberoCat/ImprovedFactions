package io.github.toberocat.improvedfactions.listeners

import io.github.toberocat.improvedfactions.database.storage.StorageManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener : Listener {

    @EventHandler
    private fun onJoin(event: PlayerJoinEvent) {
        val player: Player = event.player

        StorageManager.persistKnownPlayer(player.uniqueId, player.name)
    }
}
