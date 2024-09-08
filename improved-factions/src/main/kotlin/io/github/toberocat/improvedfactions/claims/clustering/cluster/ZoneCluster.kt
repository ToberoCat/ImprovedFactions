package io.github.toberocat.improvedfactions.claims.clustering.cluster

import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ZoneCluster(id: EntityID<Int>) : IntEntity(id), AdditionalClusterType {
    companion object : IntEntityClass<ZoneCluster>(ZoneClusters)

    var zoneType by ZoneClusters.zoneType

    override fun scheduleUpdate() = Unit
    override fun getColor() = ZoneHandler.getZone(zoneType)?.mapColor
}