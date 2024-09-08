package io.github.toberocat.improvedfactions.modules.chat.handles

import io.github.toberocat.improvedfactions.modules.chat.ChatMode
import java.util.UUID

interface ChatModuleHandle {
    fun setChatMode(playerId: UUID, chatMode: ChatMode)
    fun getChatMode(playerId: UUID): ChatMode
    fun spy(playerId: UUID)
    fun unspy(playerId: UUID)
    fun isSpying(playerId: UUID): Boolean
    fun getSpyingPlayers(): Set<UUID>
}