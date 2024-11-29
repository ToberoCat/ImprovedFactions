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
    label = "admin claim",
    category = CommandCategory.GENERAL_CATEGORY,
    module = "core",
    responses = [
        CommandResponse("factionClaimed"),
        CommandResponse("claimError"),
        CommandResponse("factionNotFound"),
        CommandResponse("noPermission")
    ]
)
abstract class ForceClaimCommand : ForceClaimCommandContext() {

    fun processPlayer(sender: Player, faction: Faction) =
        claimFaction(sender.location, faction)

    fun processConsole(sender: CommandSender, faction: Faction, world: World, blockX: Int, blockZ: Int) =
        claimFaction(world.getBlockAt(blockX, 0, blockZ).location, faction)

    private fun claimFaction(location: Location, faction: Faction): CommandProcessResult {
        faction.claim(location.chunk)
        return factionClaimed("faction" to faction.name)
    }
}