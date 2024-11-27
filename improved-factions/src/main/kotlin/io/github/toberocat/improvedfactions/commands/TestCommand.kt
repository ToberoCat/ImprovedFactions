package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.annotations.CommandCategory
import io.github.toberocat.improvedfactions.annotations.CommandResponse
import io.github.toberocat.improvedfactions.annotations.GeneratedCommandMeta
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "invite",
    description = "This is a test command.",
    category = CommandCategory.MEMBER_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("playerInvited"),
        CommandResponse("missingRequiredArgument"),
    ]
)
open class TestCommand : TestCommandContext() {
    fun process(sender: Player, condition: Boolean): CommandProcessResult {
        if (condition) {
            return playerInvited()
        }
        return missingRequiredArgument()
    }

//    fun process(sender: Player, invitedPlayer: OfflinePlayer, rank: FactionRank?): CommandProcessResult {
//        if (rank == null) {
//            return missingRequiredArgument()
//        }
//
//        return playerInvited()
//    }
}