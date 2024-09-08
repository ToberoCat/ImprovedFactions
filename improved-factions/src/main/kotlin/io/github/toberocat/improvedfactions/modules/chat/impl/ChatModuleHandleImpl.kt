package io.github.toberocat.improvedfactions.modules.chat.impl

import io.github.toberocat.improvedfactions.modules.chat.ChatMode
import io.github.toberocat.improvedfactions.modules.chat.handles.ChatModuleHandle
import java.util.*

class ChatModuleHandleImpl : ChatModuleHandle {
    private val map: MutableMap<UUID, ChatMode> = mutableMapOf()
    private val spyingPlayers: MutableSet<UUID> = mutableSetOf()

    override fun setChatMode(playerId: UUID, chatMode: ChatMode) {
        map[playerId] = chatMode
    }

    override fun getChatMode(playerId: UUID): ChatMode = map[playerId] ?: ChatMode.GLOBAL

    override fun spy(playerId: UUID) {
        spyingPlayers.add(playerId)
    }

    override fun unspy(playerId: UUID) {
        spyingPlayers.remove(playerId)
    }

    override fun isSpying(playerId: UUID) = spyingPlayers.contains(playerId)

    override fun getSpyingPlayers() = spyingPlayers
}