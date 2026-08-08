package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.FactionSnapshot
import io.github.toberocat.improvedfactions.database.storage.GameStateCommands
import io.github.toberocat.improvedfactions.database.storage.claimKey
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

    fun processPlayer(sender: Player, faction: FactionSnapshot) =
        unclaimFaction(sender, faction, sender.location)

    fun processConsole(
        sender: CommandSender,
        faction: FactionSnapshot,
        world: World,
        blockX: Int,
        blockZ: Int,
    ): CommandProcessResult? {
        val location = world.getBlockAt(blockX, 0, blockZ).location
        return unclaimFaction(sender, faction, location)
    }

    private fun unclaimFaction(sender: CommandSender, faction: FactionSnapshot, location: Location): CommandProcessResult? =
        sender.respondAfter(GameStateCommands.unclaim(location.claimKey())) {
            factionUnclaimed("faction" to faction.name)
        }
}
