package io.github.toberocat.improvedfactions.factions.ban

import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.user.FactionUser
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class FactionBan(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionBan>(FactionBans)
    var faction by Faction referencedOn FactionBans.faction
    var user by FactionUser referencedOn FactionBans.user
}
