package io.github.toberocat.improvedfactions.utils.offline

import org.jetbrains.exposed.dao.id.UUIDTable

object KnownOfflinePlayers : UUIDTable("known_offline_players") {
    val name = varchar("name", 16)
}