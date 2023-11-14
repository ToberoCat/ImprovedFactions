package io.github.toberocat.improvedfactions.permissions

import org.jetbrains.exposed.dao.id.IntIdTable

object FactionPermissions : IntIdTable("faction_permissions") {
    val rankId = integer("rank_id")
    val permission = varchar("permission", 255)
    val allowed = bool("allowed").default(false)
}