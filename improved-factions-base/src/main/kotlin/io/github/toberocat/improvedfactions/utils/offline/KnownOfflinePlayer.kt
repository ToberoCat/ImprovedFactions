package io.github.toberocat.improvedfactions.utils.offline

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class KnownOfflinePlayer(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<KnownOfflinePlayer>(KnownOfflinePlayers)

    var name by KnownOfflinePlayers.name
}