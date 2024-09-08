package io.github.toberocat.improvedfactions.claims.clustering.cluster

import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.clustering.position.ChunkPosition
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.utils.LazyUpdate
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FactionCluster(id: EntityID<Int>) : IntEntity(id), AdditionalClusterType {
    companion object : IntEntityClass<FactionCluster>(FactionClusters)

    private val unprotectedPositions = LazyUpdate {
        Cluster.findById(parentClusterId)?.let {
            PowerRaidsModule.powerRaidModule().powerModuleHandle
                .calculateUnprotectedChunks(it)
        } ?: emptySet()
    }

    var faction by Faction referencedOn FactionClusters.faction
    private var parentClusterId by FactionClusters.parentClusterId

    override fun scheduleUpdate() = unprotectedPositions.scheduleUpdate()

    fun isUnprotected(x: Int, y: Int, world: String) = ChunkPosition(x, y, world).getFactionClaim()?.let {
        it in unprotectedPositions.get()
    }

    override fun getColor() = FactionHandler.generateColor(faction.id.value)
}