package io.github.toberocat.improvedfactions.claims.clustering

import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule.Companion.powerRaidModule
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle

class Cluster(val factionId: Int,
              private val positions: MutableSet<Position> = mutableSetOf()) {
    private val powerModuleHandle: FactionPowerRaidModuleHandle = powerRaidModule().factionModuleHandle
    private val unprotectedPositions: MutableSet<Position> = mutableSetOf()
    private var lazyUpdate = true
    var centerX = 0.0
    var centerY = 0.0

    init {
        calculateCenter()
    }

    fun getReadOnlyPositions(): Set<Position> = positions.toSet()

    fun scheduleUpdate() {
        lazyUpdate = true
    }

    fun isUnprotected(x: Int, y: Int, world: String): Boolean {
        if (lazyUpdate)
            updateUnprotectedChunks()
        return Position(x, y, world, -1) in unprotectedPositions
    }

    fun isEmpty() = positions.isEmpty()

    fun removeAll(position: Set<Position>) {
        positions.removeAll(position)
        calculateCenter()
        DynmapModule.dynmapModule().dynmapModuleHandle.factionClusterChange(this)
    }

    fun addAll(positions: Set<Position>) {
        if (positions.any { it.factionId != factionId })
            throw IllegalArgumentException("All positions must belong to the same faction")

        this.positions.addAll(positions)
        calculateCenter()
        DynmapModule.dynmapModule().dynmapModuleHandle.factionClusterChange(this)
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