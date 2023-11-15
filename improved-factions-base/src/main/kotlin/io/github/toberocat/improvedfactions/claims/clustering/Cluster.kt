package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule.Companion.powerRaidModule
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle

class Cluster(val factionId: Int, val positions: MutableList<Position> = mutableListOf()) {
    private val powerModuleHandle: FactionPowerRaidModuleHandle = powerRaidModule().factionModuleHandle
    private val unprotectedPositions: MutableSet<Position> = mutableSetOf()
    private var lazyUpdate = false
    var centerX = 0.0
    var centerY = 0.0

    init {
        calculateCenter()
    }

    fun scheduleUpdate() {
        lazyUpdate = true
    }

    fun isProtected(x: Int, y: Int): Boolean {
        if (lazyUpdate)
            updateUnprotectedChunks()
        return Position(x, y, -1) in unprotectedPositions
    }

    fun isEmpty() = positions.isEmpty()
    fun remove(position: Position) {
        positions.remove(position)
        calculateCenter()
    }

    fun removeAll(unreachablePositions: Set<Position>) {
        positions.removeAll(unreachablePositions)
        calculateCenter()
    }

    fun add(position: Position) {
        if (position.factionId != factionId)
            throw IllegalArgumentException()

        positions.add(position)
        calculateCenter()
    }

    private fun calculateCenter() {
        if (positions.isEmpty())
            return

        centerX = positions.map { it.x }.average()
        centerY = positions.map { it.y }.average()
    }

    private fun updateUnprotectedChunks() {
        lazyUpdate = false
        unprotectedPositions.clear()
        powerModuleHandle.calculateUnprotectedChunks(this, unprotectedPositions)
    }
}