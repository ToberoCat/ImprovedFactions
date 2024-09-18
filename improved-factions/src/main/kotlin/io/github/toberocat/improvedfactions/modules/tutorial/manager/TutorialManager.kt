package io.github.toberocat.improvedfactions.modules.tutorial.manager

import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object TutorialManager {

    private val lockedPlayers = ConcurrentHashMap.newKeySet<UUID>()

    fun lockPlayerMovement(player: Player) {
        player.allowFlight = true
        player.isFlying = true
        lockedPlayers.add(player.uniqueId)
    }

    fun unlockPlayerMovement(player: Player) {
        player.allowFlight = false
        player.isFlying = false
        lockedPlayers.remove(player.uniqueId)
    }

    fun isPlayerMovementLocked(player: Player): Boolean {
        return lockedPlayers.contains(player.uniqueId)
    }
}
