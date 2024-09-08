package io.github.toberocat.improvedfactions.factions.ban

import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.improvedfactions.user.FactionUsers
import org.jetbrains.exposed.dao.id.IntIdTable

object FactionBans : IntIdTable("faction_bans") {
    val faction = reference("faction", Factions)
    val user = reference("user", FactionUsers)
}