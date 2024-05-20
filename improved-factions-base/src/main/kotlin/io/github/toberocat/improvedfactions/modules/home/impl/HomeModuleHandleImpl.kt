package io.github.toberocat.improvedfactions.modules.home.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import io.github.toberocat.improvedfactions.modules.home.HomeModule.getHome
import io.github.toberocat.improvedfactions.modules.home.data.FactionHome
import io.github.toberocat.improvedfactions.modules.home.handles.HomeModuleHandle
import io.github.toberocat.improvedfactions.utils.PlayerTeleporter
import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class HomeModuleHandleImpl : HomeModuleHandle {
    override fun setHome(faction: Faction, location: Location) {
        if (location.getFactionClaim()?.factionId?.equals(faction.id.value) != true) {
            throw CommandException("home.messages.not-in-claim", mapOf())
        }

        val worldName = location.world?.name ?: throw CommandException("home.messages.invalid-world", mapOf())
        val factionHome = getOrCreateHome(faction.id.value)
        factionHome.x = location.x
        factionHome.y = location.y
        factionHome.z = location.z
        factionHome.world = worldName
        DynmapModule.dynmapModule().dynmapModuleHandle.factionHomeChange(faction, location)
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