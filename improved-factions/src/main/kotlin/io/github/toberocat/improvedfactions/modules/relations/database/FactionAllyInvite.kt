package io.github.toberocat.improvedfactions.modules.relations.database

import io.github.toberocat.improvedfactions.factions.Faction
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FactionAllyInvite(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionAllyInvite>(FactionAllyInvites)

    var sourceFaction by Faction referencedOn FactionAllyInvites.sourceFaction
    var targetFaction by Faction referencedOn FactionAllyInvites.targetFaction
    var expirationDate by FactionAllyInvites.expirationDate
}