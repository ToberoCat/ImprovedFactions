package io.github.toberocat.improvedfactions.modules.relations.database

import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.improvedfactions.modules.relations.RelationType
import org.jetbrains.exposed.dao.id.IntIdTable

object FactionRelations : IntIdTable("faction_relations") {
    val sourceFaction = reference("source_faction_id", Factions)
    val targetFaction = reference("target_faction_id", Factions)
    val relationType = enumeration("relation_type", RelationType::class)
}