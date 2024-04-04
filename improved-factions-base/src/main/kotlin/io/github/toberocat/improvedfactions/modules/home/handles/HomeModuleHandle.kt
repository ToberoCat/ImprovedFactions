package io.github.toberocat.improvedfactions.modules.home.handles

import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Location
import org.bukkit.entity.Player

interface HomeModuleHandle {
    fun setHome(faction: Faction, location: Location): Boolean
    fun getHome(faction: Faction): Location?
    fun teleportToFactionHome(faction: Faction, player: Player): Boolean
}