package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "admin leave",
    category = CommandCategory.GENERAL_CATEGORY,
    module = "core",
    responses = [
        CommandResponse("forceLeaveSuccess"),
        CommandResponse("factionNotFound"),
        CommandResponse("noPermission"),
        CommandResponse("ownershipTransferred"),
        CommandResponse("factionDeleted"),
        CommandResponse("claimError")
    ]
)
abstract class ForceLeaveCommand : ForceLeaveCommandContext() {

    fun processConsole(
        sender: CommandSender,
        target: OfflinePlayer,
    ) = leaveFaction(target)

    private fun leaveFaction(player: OfflinePlayer): CommandProcessResult {
        val user = player.factionUser()
        val faction = user.faction() ?: return factionNotFound()

        if (!user.isFactionOwner()) {
            faction.leave(player.uniqueId)
            return forceLeaveSuccess()
        }

        return when (val nextBestOwner = determineNextBestOwner(faction, player)) {
            null -> {
                faction.delete()
                factionDeleted()
            }

            else -> {
                faction.transferOwnership(nextBestOwner.uniqueId)
                ownershipTransferred("player" to (nextBestOwner.offlinePlayer().name ?: "Unknown"))
            }
        }
    }

    private fun determineNextBestOwner(faction: Faction, target: OfflinePlayer) = faction.members()
        .sortedBy { it.rank().priority }
        .firstOrNull { it.uniqueId != target.uniqueId }
}