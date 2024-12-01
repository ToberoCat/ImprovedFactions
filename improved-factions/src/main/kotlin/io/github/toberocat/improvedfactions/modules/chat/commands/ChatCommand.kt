package io.github.toberocat.improvedfactions.modules.chat.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.modules.chat.ChatModule
import io.github.toberocat.improvedfactions.modules.chat.ChatModule.toggleChatMode
import io.github.toberocat.improvedfactions.modules.chat.handles.ChatModuleHandle
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "chat",
    category = CommandCategory.COMMUNICATION_CATEGORY,
    module = ChatModule.MODULE_NAME,
    responses = [
        CommandResponse("chatModeToggled"),
        CommandResponse("noFaction")
    ]
)
abstract class ChatCommand : ChatCommandContext() {

    fun process(player: Player): CommandProcessResult {
        if (!player.factionUser().isInFaction()) {
            return noFaction()
        }

        val nextMode = player.toggleChatMode()
        return chatModeToggled("mode" to nextMode.name.lowercase())
    }
}