package io.github.toberocat.improvedfactions.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

const val noFactionId = -1

/** Internal schema entity; runtime consumers use UserSnapshot. */
internal class FactionUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionUser>(FactionUsers)

    var uniqueId by FactionUsers.uniqueId
    var factionId by FactionUsers.factionId
    var assignedRank by FactionUsers.assignedRank
}
