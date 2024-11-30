package io.github.toberocat.improvedfactions.modules.chat

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.CommandProcessor
import io.github.toberocat.improvedfactions.commands.processor.chatCommandProcessors
import io.github.toberocat.improvedfactions.modules.Module
import io.github.toberocat.improvedfactions.modules.chat.commands.ChatCommand
import io.github.toberocat.improvedfactions.modules.chat.commands.ChatCommandProcessor
import io.github.toberocat.improvedfactions.modules.chat.commands.ChatSpyCommand
import io.github.toberocat.improvedfactions.modules.chat.commands.ChatSpyCommandProcessor
import io.github.toberocat.improvedfactions.modules.chat.config.ChatModuleConfig
import io.github.toberocat.improvedfactions.modules.chat.handles.ChatModuleHandle
import io.github.toberocat.improvedfactions.modules.chat.handles.DummyChatModuleHandle
import io.github.toberocat.improvedfactions.modules.chat.impl.ChatModuleHandleImpl
import io.github.toberocat.improvedfactions.modules.chat.listener.ChatListener
import io.github.toberocat.toberocore.command.CommandExecutor
import org.bukkit.entity.Player

object ChatModule : Module {
    const val MODULE_NAME = "chat"
    override val moduleName = MODULE_NAME
    override var isEnabled = false

    var chatModuleHandle: ChatModuleHandle = DummyChatModuleHandle()
    private val chatModuleConfig = ChatModuleConfig()

    override fun getCommandProcessors(plugin: ImprovedFactionsPlugin): List<CommandProcessor> =
        chatCommandProcessors(plugin)

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        chatModuleHandle = ChatModuleHandleImpl()
        plugin.registerListeners(
            ChatListener(chatModuleConfig, chatModuleHandle)
        )
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        chatModuleConfig.reload(plugin, plugin.config)
    }

    fun Player.resetChatMode() = chatModuleHandle.setChatMode(uniqueId, ChatMode.GLOBAL)

    fun Player.sendFactionChat(message: String) = ChatMode.FACTION.sendIntoChat(this, message)

    fun Player.toggleChatMode(): ChatMode {
        val current = chatModuleHandle.getChatMode(uniqueId).ordinal
        val values = enumValues<ChatMode>()
        val nextChatMode = values[(current + 1) % values.size]
        chatModuleHandle.setChatMode(uniqueId, nextChatMode)
        return nextChatMode
    }

    fun chatPair() = MODULE_NAME to this
}