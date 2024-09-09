package io.github.toberocat.improvedfactions.modules.relations.database

import io.github.toberocat.improvedfactions.factions.Faction
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FactionRelation(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionRelation>(FactionRelations)

    var sourceFaction by Faction referencedOn FactionRelations.sourceFaction
    var targetFaction by Faction referencedOn FactionRelations.targetFaction
    var relationType by FactionRelations.relationType
}