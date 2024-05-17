package io.github.toberocat.improvedfactions.managers

import java.util.UUID

object ByPassManager {
    private val bypassedPlayers = mutableSetOf<UUID>()

    fun isBypassing(playerName: UUID) = playerName in bypassedPlayers

    fun addBypass(playerName: UUID) {
        bypassedPlayers.add(playerName)
    }

    fun removeBypass(playerName: UUID) {
        bypassedPlayers.remove(playerName)
    }
}