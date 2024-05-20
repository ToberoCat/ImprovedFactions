package io.github.toberocat.improvedfactions.modules.home.handles

import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Location
import org.bukkit.entity.Player

class DummyHomeModuleHandle : HomeModuleHandle {
    override fun setHome(faction: Faction, location: Location) = Unit
    override fun getHome(faction: Faction): Location? = null
    override fun teleportToFactionHome(faction: Faction, player: Player) = false
}