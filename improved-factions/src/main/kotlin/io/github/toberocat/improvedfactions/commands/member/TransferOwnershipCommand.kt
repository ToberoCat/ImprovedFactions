package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandConfirmation
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@CommandConfirmation
@GeneratedCommandMeta(
    label = "transferowner",
    category = CommandCategory.MEMBER_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("ownershipTransferred"),
        CommandResponse("notOwner"),
        CommandResponse("notInFaction"),
    ]
)
abstract class TransferOwnershipCommand : TransferOwnershipCommandContext() {

    fun process(player: Player, targetUser: OfflinePlayer): CommandProcessResult? {
        val user = player.cachedUser()
        val faction = user.faction()
            ?: return notInFaction()

        if (!user.isFactionOwner()) {
            return notOwner()
        }

        if (targetUser.cachedUser().factionId != faction.id) return notInFaction()
        val targetName = targetUser.name ?: "Unknown"
        return player.respondAfter(GameStateCommands.transferOwnership(
            faction.id, player.uniqueId, targetUser.uniqueId
        )) { ownershipTransferred(
            "newOwner" to targetName,
            "factionName" to faction.name
        ) }
    }
}
