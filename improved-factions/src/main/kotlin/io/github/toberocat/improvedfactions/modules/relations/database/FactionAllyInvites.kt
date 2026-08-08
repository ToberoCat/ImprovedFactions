package io.github.toberocat.improvedfactions.modules.relations.database

import io.github.toberocat.improvedfactions.factions.Factions
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object FactionAllyInvites : IntIdTable("faction_ally_invites") {
    var inviteExpiresInMinutes = 5

    val sourceFaction = reference("source_faction_id", Factions)
    val targetFaction = reference("target_faction_id", Factions)

    val expirationDate = datetime("expiration_date")

}
