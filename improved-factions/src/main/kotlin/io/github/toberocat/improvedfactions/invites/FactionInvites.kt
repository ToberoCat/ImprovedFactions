package io.github.toberocat.improvedfactions.invites

import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object FactionInvites : IntIdTable("faction_invites") {
    val inviterId = integer("inviter_id")
    val invitedId = integer("invited_id")
    val factionId = integer("faction_id")
    val rankId = integer("rank_id").default(FactionRankHandler.guestRankId)
    val expirationDate = datetime("expiration_date")

}
