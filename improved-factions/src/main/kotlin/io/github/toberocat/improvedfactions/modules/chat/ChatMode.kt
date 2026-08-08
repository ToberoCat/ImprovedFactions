package io.github.toberocat.improvedfactions.modules.chat

import io.github.toberocat.improvedfactions.database.storage.cachedUser
import io.github.toberocat.improvedfactions.database.storage.faction
import io.github.toberocat.improvedfactions.factions.LocalizedMessage
import io.github.toberocat.improvedfactions.messages.MessageBroker
import org.bukkit.entity.Player

enum class ChatMode {
    GLOBAL {
        override fun sendIntoChat(player: Player, message: String) {
            player.chat(message)
        }
    },
    FACTION {
        override fun sendIntoChat(player: Player, message: String) {
                val faction = player.cachedUser().faction() ?: throw IllegalArgumentException("Player must be in faction")
                MessageBroker.send(faction.id, LocalizedMessage("chat.faction.template", mapOf(
                    "player" to player.name,
                    "message" to message,
                    "faction" to faction.name
                )))
        }

    };

    abstract fun sendIntoChat(player: Player, message: String)
}
