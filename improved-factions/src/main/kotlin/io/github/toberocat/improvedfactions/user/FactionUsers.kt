package io.github.toberocat.improvedfactions.user

import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
object FactionUsers : IntIdTable("faction_users") {
    val uniqueId = uuid("uniqueId")
    val factionId = integer("faction_id")
    val assignedRank = integer("rank_id")
        .default(FactionRankHandler.guestRankId)
}