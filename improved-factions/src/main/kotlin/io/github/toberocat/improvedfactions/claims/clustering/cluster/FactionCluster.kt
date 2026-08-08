package io.github.toberocat.improvedfactions.claims.clustering.cluster

import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class FactionCluster(id: EntityID<Int>) : IntEntity(id), AdditionalClusterType {
    companion object : IntEntityClass<FactionCluster>(FactionClusters)

    var faction by Faction referencedOn FactionClusters.faction
    private var parentClusterId by FactionClusters.parentClusterId

    override fun scheduleUpdate() = Unit

    override fun getColor() = FactionHandler.generateColor(faction.id.value)
}
