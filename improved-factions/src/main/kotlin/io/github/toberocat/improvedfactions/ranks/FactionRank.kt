package io.github.toberocat.improvedfactions.ranks

import io.github.toberocat.improvedfactions.permissions.FactionPermission
import io.github.toberocat.improvedfactions.permissions.FactionPermissions
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

/** Internal schema entity; runtime consumers use RankSnapshot. */
internal class FactionRank(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionRank>(FactionRanks)

    var factionId by FactionRanks.factionId
    var name by FactionRanks.name
    var priority by FactionRanks.priority

    fun permissions(): SizedIterable<FactionPermission> =
        FactionPermission.find { FactionPermissions.rankId eq id.value }
}
