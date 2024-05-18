package io.github.toberocat.improvedfactions.modules.chat.listener

import io.github.toberocat.improvedfactions.modules.chat.ChatMode
import io.github.toberocat.improvedfactions.modules.chat.handles.ChatModuleHandle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener(private val chatModuleHandle: ChatModuleHandle) : Listener {
    @EventHandler
    private fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val chatMode = chatModuleHandle.getChatMode(player.uniqueId)
        if (chatMode == ChatMode.GLOBAL) {
            return
        }

        val message = event.message
        event.isCancelled = true
        chatMode.sendIntoChat(player, message)
    }
}