package io.github.toberocat.improvedfactions.modules.chat.listener

import io.github.toberocat.improvedfactions.modules.chat.ChatMode
import io.github.toberocat.improvedfactions.modules.chat.config.ChatModuleConfig
import io.github.toberocat.improvedfactions.modules.chat.handles.ChatModuleHandle
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.toOfflinePlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener(private val config: ChatModuleConfig,
                   private val chatModuleHandle: ChatModuleHandle) : Listener {
    @EventHandler
    private fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val chatMode = chatModuleHandle.getChatMode(player.uniqueId)
        if (chatMode == ChatMode.GLOBAL) return

        val message = event.message
        event.isCancelled = true
        if (config.logFactionChatsToConsole) {
            val chatModeName = chatMode.name.lowercase()
            player.server.consoleSender.sendMessage("[FactionChat] ${player.name} ($chatModeName): $message")
        }

        chatModuleHandle.getSpyingPlayers().forEach { spy ->
            spy.toOfflinePlayer().player?.sendMessage("[FactionChat] ${player.name} (${chatMode.name}): $message")
        }

        chatMode.sendIntoChat(player, message)
    }
}