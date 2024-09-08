package io.github.toberocat.improvedfactions.utils.options.limit

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PlayerUsageLimit(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlayerUsageLimit>(PlayerUsageLimits)

    var registry by PlayerUsageLimits.registry
    var used by PlayerUsageLimits.used
    var playerId by PlayerUsageLimits.playerId
}