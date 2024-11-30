package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "admin unclaim",
    category = CommandCategory.ADMIN_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("factionUnclaimed"),
        CommandResponse("unclaimError"),
        CommandResponse("factionNotFound"),
        CommandResponse("noPermission")
    ]
)
abstract class ForceUnclaimCommand : ForceUnclaimCommandContext() {

    fun processPlayer(sender: Player, faction: Faction) =
        unclaimFaction(faction, sender.location)

    fun processConsole(
        sender: CommandSender,
        faction: Faction,
        world: World,
        blockX: Int,
        blockZ: Int,
    ): CommandProcessResult {
        val location = world.getBlockAt(blockX, 0, blockZ).location
        return unclaimFaction(faction, location)
    }

    private fun unclaimFaction(faction: Faction, location: Location): CommandProcessResult {
        faction.unclaim(location.chunk)
        return factionUnclaimed("faction" to faction.name)
    }
}