package io.github.toberocat.improvedfactions.modules.tutorial.listener

import io.github.toberocat.improvedfactions.modules.tutorial.manager.TutorialManager
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerMoveEvent

class PlayerListener : Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        if (!TutorialManager.isPlayerMovementLocked(player)) return

        event.isCancelled = true
    }
}
