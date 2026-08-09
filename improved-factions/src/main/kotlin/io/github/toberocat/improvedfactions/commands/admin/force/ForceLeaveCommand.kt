package io.github.toberocat.improvedfactions.commands.admin.force

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.cancelledCommandResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.api.events.FactionDeleteEvent
import io.github.toberocat.improvedfactions.api.events.FactionLeaveEvent
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.power.PowerRaidsModule
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.Bukkit

@PermissionConfig(config = PermissionConfigurations.OP_ONLY)
@GeneratedCommandMeta(
    label = "admin leave",
    category = CommandCategory.GENERAL_CATEGORY,
    module = "base",
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
    ) = leaveFaction(sender, target)

    private fun leaveFaction(sender: CommandSender, player: OfflinePlayer): CommandProcessResult? {
        val user = player.cachedUser()
        val faction = user.faction() ?: return factionNotFound()

        if (!user.isFactionOwner()) {
            val event = FactionLeaveEvent(faction, user)
            Bukkit.getPluginManager().callEvent(event)
            if (event.isCancelled) return cancelledCommandResult()
            return sender.respondAfter(GameStateCommands.setUserFaction(
                player.uniqueId, noFactionId, 0, PowerRaidsModule.config.baseMemberConstant
            )) {
                forceLeaveSuccess()
            }
        }

        return when (val nextBestOwner = determineNextBestOwner(faction, player)) {
            null -> {
                val event = FactionDeleteEvent(faction)
                Bukkit.getPluginManager().callEvent(event)
                if (event.isCancelled) return cancelledCommandResult()
                return sender.respondAfter(GameStateCommands.deleteFaction(faction.id)) { factionDeleted() }
            }

            else -> {
                val nextOwnerName = org.bukkit.Bukkit.getOfflinePlayer(nextBestOwner.uniqueId).name ?: "Unknown"
                return sender.respondAfter(GameStateCommands.transferOwnership(
                    faction.id,
                    player.uniqueId,
                    nextBestOwner.uniqueId,
                    removePreviousOwner = true,
                    memberPowerConstant = PowerRaidsModule.config.baseMemberConstant,
                )) {
                    ownershipTransferred("player" to nextOwnerName)
                }
            }
        }
    }

    private fun determineNextBestOwner(faction: FactionSnapshot, target: OfflinePlayer) = StorageManager.cache
        .factionMembers(faction.id).mapNotNull(StorageManager.cache::user)
        .sortedByDescending { it.rankPriority }
        .firstOrNull { it.uniqueId != target.uniqueId }
}
