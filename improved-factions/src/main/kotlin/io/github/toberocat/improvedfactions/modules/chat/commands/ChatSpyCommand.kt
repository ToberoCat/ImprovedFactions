package io.github.toberocat.improvedfactions.modules.chat.commands

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.modules.chat.ChatModule
import io.github.toberocat.improvedfactions.modules.chat.handles.ChatModuleHandle
import io.github.toberocat.improvedfactions.translation.sendLocalized
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "spy",
    category = CommandCategory.GENERAL_CATEGORY,
    module = ChatModule.MODULE_NAME,
    responses = [
        CommandResponse("spyEnabled"),
        CommandResponse("spyDisabled")
    ]
)
abstract class ChatSpyCommand : ChatSpyCommandContext() {
    fun process(player: Player) = when {
        ChatModule.chatModuleHandle.isSpying(player.uniqueId) -> {
            ChatModule.chatModuleHandle.unspy(player.uniqueId)
            spyDisabled()
        }

        else -> {
            ChatModule.chatModuleHandle.spy(player.uniqueId)
            spyEnabled()
        }
    }
}