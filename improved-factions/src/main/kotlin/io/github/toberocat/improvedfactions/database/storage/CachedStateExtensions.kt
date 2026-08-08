package io.github.toberocat.improvedfactions.database.storage

import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.OfflinePlayer
import java.util.UUID

fun UUID.cachedUser(): UserSnapshot = StorageManager.cache.user(this)
    ?: UserSnapshot(this, noFactionId, "Guest")

fun OfflinePlayer.cachedUser(): UserSnapshot = uniqueId.cachedUser()

fun UserSnapshot.faction(): FactionSnapshot? = StorageManager.cache.faction(factionId)

fun UserSnapshot.isInFaction(): Boolean = factionId != noFactionId

fun UserSnapshot.isFactionOwner(): Boolean = faction()?.owner == uniqueId

fun UserSnapshot.hasPermission(permission: String): Boolean =
    isFactionOwner() || permission in permissions

fun UserSnapshot.canManage(rank: RankSnapshot): Boolean =
    isFactionOwner() || (rank.factionId == factionId && rank.priority < rankPriority)
