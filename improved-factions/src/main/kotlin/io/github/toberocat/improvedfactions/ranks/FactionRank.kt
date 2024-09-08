package io.github.toberocat.improvedfactions.ranks

import io.github.toberocat.improvedfactions.permissions.FactionPermission
import io.github.toberocat.improvedfactions.permissions.FactionPermissions
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.FactionUsers
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class FactionRank(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionRank>(FactionRanks)

    var factionId by FactionRanks.factionId
    var name by FactionRanks.name
    var priority by FactionRanks.priority

    override fun delete() {
        permissions().forEach { it.delete() }
        super.delete()
    }

    fun canManage(rank: FactionRank): Boolean = rank.priority < priority

    fun countAssignedUsers(): Long = FactionUser.count(FactionUsers.assignedRank eq id.value)

    fun getAssignedUserPreview(maxUsers: Int = 5): List<OfflinePlayer> =
        FactionUser.find { FactionUsers.assignedRank eq id.value }
            .limit(maxUsers)
            .map { Bukkit.getOfflinePlayer(it.uniqueId) }

    fun permissions(): SizedIterable<FactionPermission> =
        FactionPermission.find { FactionPermissions.rankId eq id.value }
}