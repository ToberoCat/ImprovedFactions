package io.github.toberocat.improvedfactions.factions

import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
object Factions : IntIdTable("factions") {
    val name = varchar("name", BaseModule.config.maxNameLength)
    val owner = uuid("owner")
    val accumulatedPower = integer("accumulated_power").default(0)
    val maxPower = integer("max_power").default(50)
    val defaultRank = integer("default_rank").default(FactionRankHandler.guestRankId)
    val base64Icon = varchar("icon_base64", BaseModule.config.maxFactionIconLength).nullable()
    val factionJoinType = enumeration("join_type", FactionJoinType::class)
        .default(FactionJoinType.INVITE_ONLY)

    fun handleQueues() {
        Faction.all().forEach { FactionHandler.createListenersFor(it) }
    }
}