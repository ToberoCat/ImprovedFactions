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
    label = "admin claim",
    category = CommandCategory.GENERAL_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("factionClaimed"),
        CommandResponse("claimError"),
        CommandResponse("factionNotFound"),
        CommandResponse("noPermission")
    ]
)
abstract class ForceClaimCommand : ForceClaimCommandContext() {

    fun processPlayer(sender: Player, faction: FactionSnapshot) =
        claimFaction(sender, sender.location, faction)

    fun processConsole(sender: CommandSender, faction: FactionSnapshot, world: World, blockX: Int, blockZ: Int) =
        claimFaction(sender, world.getBlockAt(blockX, 0, blockZ).location, faction)

    private fun claimFaction(sender: CommandSender, location: Location, faction: FactionSnapshot): CommandProcessResult? =
        sender.respondAfter(GameStateCommands.claim(location.claimKey(), faction.id)) {
            factionClaimed("faction" to faction.name)
        }
}
