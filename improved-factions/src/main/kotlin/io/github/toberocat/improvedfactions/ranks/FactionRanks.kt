package io.github.toberocat.improvedfactions.ranks

import org.jetbrains.exposed.dao.id.IntIdTable

object FactionRanks : IntIdTable("faction_ranks") {
    var maxRankNameLength = 50
    var rankNameRegex = Regex("[a-zA-Z]*")

    val factionId = integer("faction_id")
    val name = varchar("rank_name", maxRankNameLength)
    var priority = integer("priority")
}