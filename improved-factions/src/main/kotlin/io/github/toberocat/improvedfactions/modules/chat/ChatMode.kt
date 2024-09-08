package io.github.toberocat.improvedfactions.modules.chat

import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

enum class ChatMode {
    GLOBAL {
        override fun sendIntoChat(player: Player, message: String) {
            player.chat(message)
        }
    },
    FACTION {
        override fun sendIntoChat(player: Player, message: String) {
            loggedTransaction {
                val faction = player.factionUser().faction() ?: throw IllegalArgumentException("Player must be in faction")
                faction.broadcast("chat.faction.template", mapOf(
                    "player" to player.name,
                    "message" to message,
                    "faction" to faction.name
                ))
            }
        }

    };

    abstract fun sendIntoChat(player: Player, message: String)
}