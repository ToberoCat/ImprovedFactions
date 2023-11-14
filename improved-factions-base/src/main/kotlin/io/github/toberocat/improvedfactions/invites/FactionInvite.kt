package io.github.toberocat.improvedfactions.invites

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FactionInvite(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionInvite>(FactionInvites)

    var inviterId by FactionInvites.inviterId
    var invitedId by FactionInvites.invitedId
    var factionId by FactionInvites.factionId
    var rankId by FactionInvites.rankId
    var expirationDate by FactionInvites.expirationDate
}