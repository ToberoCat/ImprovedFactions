package io.github.toberocat.improvedfactions.modules.chat.impl

import io.github.toberocat.improvedfactions.modules.chat.ChatMode
import io.github.toberocat.improvedfactions.modules.chat.handles.ChatModuleHandle
import java.util.*

class ChatModuleHandleImpl : ChatModuleHandle {
    private val map: MutableMap<UUID, ChatMode> = mutableMapOf()
    override fun setChatMode(playerId: UUID, chatMode: ChatMode) {
        map[playerId] = chatMode
    }

    override fun getChatMode(playerId: UUID): ChatMode = map[playerId] ?: ChatMode.GLOBAL
}