package io.github.toberocat.improvedfactions.permissions

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FactionPermission(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<FactionPermission>(FactionPermissions)

    var rankId by FactionPermissions.rankId
    var permission by FactionPermissions.permission
    var allowed by FactionPermissions.allowed
}