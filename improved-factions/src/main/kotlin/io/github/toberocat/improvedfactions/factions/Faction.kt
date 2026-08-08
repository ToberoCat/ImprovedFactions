package io.github.toberocat.improvedfactions.factions

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID

/**
 * Internal schema entity retained for Exposed's startup schema mapping and legacy clustering tests.
 * Runtime gameplay code must use immutable snapshots and GameStateCommands instead.
 */
internal class Faction(id: EntityID<Int>) : IntEntity(id) {
    companion object : EntityClass<Int, Faction>(Factions)

    var owner by Factions.owner
    var defaultRank by Factions.defaultRank
    var name by Factions.name
    var accumulatedPower by Factions.accumulatedPower
    var maxPower by Factions.maxPower
    var factionJoinType by Factions.factionJoinType
    var base64Icon by Factions.base64Icon

    fun generateColor() = FactionHandler.generateColor(id.value)
}
