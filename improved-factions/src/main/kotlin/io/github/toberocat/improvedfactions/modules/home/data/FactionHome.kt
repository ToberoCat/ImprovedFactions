package io.github.toberocat.improvedfactions.modules.home.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FactionHome(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionHome>(FactionHomes)

    var x by FactionHomes.x
    var y by FactionHomes.y
    var z by FactionHomes.z
    var world by FactionHomes.world
}