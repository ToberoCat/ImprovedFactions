package io.github.toberocat.improvedfactions.modules.home.data

import org.jetbrains.exposed.dao.id.IntIdTable

object FactionHomes : IntIdTable("faction_homes") {
    val x = double("x")
    val y = double("y")
    val z = double("z")
    val world = varchar("world", 255)
}