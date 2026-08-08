package io.github.toberocat.improvedfactions.utils.options.limit

import org.jetbrains.exposed.dao.id.IntIdTable

object PlayerUsageLimits : IntIdTable("player_usage_limits") {
    val registry = varchar("registry", 30)
    val playerId = uuid("player_id")
    val used = integer("used").default(0)
}
