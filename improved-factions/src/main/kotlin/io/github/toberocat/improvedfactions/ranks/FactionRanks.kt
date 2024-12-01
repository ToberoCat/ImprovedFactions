package io.github.toberocat.improvedfactions.ranks

import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.jetbrains.exposed.dao.id.IntIdTable

object FactionRanks : IntIdTable("faction_ranks") {
    val factionId = integer("faction_id")
    val name = varchar("rank_name", BaseModule.config.maxRankNameLength)
    var priority = integer("priority")
}