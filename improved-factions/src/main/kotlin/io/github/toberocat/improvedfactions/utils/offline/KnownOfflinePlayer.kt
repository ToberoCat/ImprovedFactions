package io.github.toberocat.improvedfactions.utils.offline

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import io.github.toberocat.improvedfactions.database.DatabaseManager.refreshStorageCacheAfterCommit

internal class KnownOfflinePlayer(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<KnownOfflinePlayer>(KnownOfflinePlayers)

    private var storedName by KnownOfflinePlayers.name
    var name
        get() = storedName
        set(value) {
            storedName = value
            refreshStorageCacheAfterCommit()
        }
}
