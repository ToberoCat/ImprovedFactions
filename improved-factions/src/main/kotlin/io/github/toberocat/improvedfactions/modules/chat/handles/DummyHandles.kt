package io.github.toberocat.improvedfactions.modules.chat.handles

import io.github.toberocat.improvedfactions.modules.chat.ChatMode
import java.util.*

class DummyChatModuleHandle: ChatModuleHandle {
    override fun setChatMode(playerId: UUID, chatMode: ChatMode) = Unit

    override fun getChatMode(playerId: UUID) = ChatMode.GLOBAL
}