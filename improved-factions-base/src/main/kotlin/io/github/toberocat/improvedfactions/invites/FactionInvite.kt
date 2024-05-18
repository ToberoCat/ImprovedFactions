package io.github.toberocat.improvedfactions.invites

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.time.Duration

class FactionInvite(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionInvite>(FactionInvites)

    var inviterId by FactionInvites.inviterId
    var invitedId by FactionInvites.invitedId
    var factionId by FactionInvites.factionId
    var rankId by FactionInvites.rankId
    var expirationDate by FactionInvites.expirationDate

    fun expiresInFormatted() = formatDuration(expirationDate.toInstant(TimeZone.UTC) - Clock.System.now())

    private fun formatDuration(duration: Duration): String {
        val minutes = duration.inWholeSeconds / 60
        val seconds = duration.inWholeSeconds % 60
        return String.format("%02dm %02ds", minutes, seconds)
    }
}