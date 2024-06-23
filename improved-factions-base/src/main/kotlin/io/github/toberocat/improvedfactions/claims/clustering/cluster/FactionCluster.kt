package io.github.toberocat.improvedfactions.claims.clustering.cluster

import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.modules.power.handles.FactionPowerRaidModuleHandle
import io.github.toberocat.improvedfactions.utils.LazyUpdate
import java.util.*

class FactionCluster(val factionId: Int, id: UUID, positions: MutableSet<ChunkPosition>) : Cluster(id, positions) {
    private val powerModuleHandle: FactionPowerRaidModuleHandle = PowerRaidsModule.powerRaidModule().powerModuleHandle
    private val unprotectedPositions = LazyUpdate(mutableSetOf()) {
        mutableSetOf<ChunkPosition>().apply {
            powerModuleHandle.calculateUnprotectedChunks(this@FactionCluster, this)
        }
    }

    override fun scheduleUpdate() = unprotectedPositions.scheduleUpdate()

    fun isUnprotected(x: Int, y: Int, world: String) = ChunkPosition(x, y, world) in unprotectedPositions.get()

    override fun getColor() = FactionHandler.generateColor(factionId)

}