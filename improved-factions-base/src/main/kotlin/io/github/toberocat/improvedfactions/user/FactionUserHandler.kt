package io.github.toberocat.improvedfactions.user

import org.bukkit.OfflinePlayer
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction 
import java.util.*

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
const val noFactionId = -1

fun OfflinePlayer.factionUser(): FactionUser = uniqueId.factionUser()

fun UUID.factionUser(): FactionUser {
    val id = this
    return loggedTransaction {
        return@loggedTransaction FactionUser.find { FactionUsers.uniqueId eq id }.firstOrNull() ?: FactionUser.new {
            uniqueId = id
            factionId = noFactionId
        }
    }
}
