package io.github.toberocat.improvedfactions.user

import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.permissions.FactionPermission
import io.github.toberocat.improvedfactions.permissions.FactionPermissions
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.ranks.FactionRankHandler
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
class FactionUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionUser>(FactionUsers)

    var uniqueId by FactionUsers.uniqueId
    var factionId by FactionUsers.factionId
    var assignedRank by FactionUsers.assignedRank

    fun offlinePlayer(): OfflinePlayer = Bukkit.getOfflinePlayer(uniqueId)

    fun player(): Player? = Bukkit.getPlayer(uniqueId)

    fun isInFaction(): Boolean = factionId != noFactionId

    fun faction(): Faction? = Faction.findById(factionId)

    fun canManage(user: FactionUser): Boolean {
        if (user.uniqueId == uniqueId)
            return false
        return canManage(user.rank())
    }

    fun canManage(rank: FactionRank): Boolean {
        if (faction()?.owner == uniqueId)
            return true
        return rank().canManage(rank)
    }

    fun hasPermission(permission: String): Boolean {
        if (faction()?.owner == uniqueId)
            return true

        return FactionPermission.count(
            FactionPermissions.rankId eq assignedRank and
                    (FactionPermissions.permission eq permission)
        ) == 1L
    }

    fun rank(): FactionRank = FactionRank.findById(assignedRank) ?: FactionRankHandler.guestRank
}