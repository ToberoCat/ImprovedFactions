package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandConfirmation
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.user.FactionUser
import io.github.toberocat.improvedfactions.user.factionUser
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

    fun process(player: Player, targetUser: FactionUser): CommandProcessResult {
        val faction = player.factionUser().faction()
            ?: return notInFaction()

        if (!player.factionUser().isFactionOwner()) {
            return notOwner()
        }

        faction.transferOwnership(targetUser.uniqueId)
        return ownershipTransferred(
            "newOwner" to (targetUser.offlinePlayer().name ?: "Unknown"),
            "factionName" to faction.name
        )
    }
}
