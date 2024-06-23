package io.github.toberocat.improvedfactions.utils.offline

import io.github.toberocat.improvedfactions.factions.Factions
import org.jetbrains.exposed.dao.id.UUIDTable

object KnownOfflinePlayers : UUIDTable() {
    val name = Factions.varchar("name", 16)
}