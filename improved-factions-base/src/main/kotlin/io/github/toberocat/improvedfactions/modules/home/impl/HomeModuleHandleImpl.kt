package io.github.toberocat.improvedfactions.modules.home.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.home.HomeModule.getHome
import io.github.toberocat.improvedfactions.modules.home.data.FactionHome
import io.github.toberocat.improvedfactions.modules.home.handles.HomeModuleHandle
import io.github.toberocat.improvedfactions.utils.PlayerTeleporter
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class HomeModuleHandleImpl : HomeModuleHandle {
    override fun setHome(faction: Faction, location: Location): Boolean {
        // ToDo: Validate home location
        val worldName = location.world?.name ?: return false
        val factionHome = getOrCreateHome(faction.id.value)
        factionHome.x = location.x
        factionHome.y = location.y
        factionHome.z = location.z
        factionHome.world = worldName

        return true
    }

    override fun getHome(faction: Faction) = FactionHome.findById(faction.id.value)?.let {
        Bukkit.getWorld(it.world)?.let { world ->
            Location(world, it.x, it.y, it.z)
        }
    }

    override fun teleportToFactionHome(faction: Faction, player: Player): Boolean {
        val location = faction.getHome() ?: return false
        PlayerTeleporter(
            ImprovedFactionsPlugin.instance,
            player,
            "home.messages.teleporting-title",
            "home.messages.teleporting-subtitle",
            { player.teleport(location) }
        ).startTeleport()

        return true
    }

    private fun getOrCreateHome(factionId: Int) = FactionHome.findById(factionId) ?: FactionHome.new(factionId) {
        x = 0.0
        y = 0.0
        z = 0.0
        world = "world"
    }
}