package io.github.toberocat.improvedfactions.utils.options.limit

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.and
import java.util.UUID

object PlayerUsageLimits : IntIdTable("player_usage_limits") {
    val registry = varchar("registry", 30)
    val playerId = uuid("player_id")
    val used = integer("used").default(0)

    fun getUsageLimit(registry: String, playerId: UUID) = PlayerUsageLimit.find {
        PlayerUsageLimits.registry eq registry and (PlayerUsageLimits.playerId eq playerId)
    }.firstOrNull() ?: PlayerUsageLimit.new {
        this.registry = registry
        this.playerId = playerId
    }
}