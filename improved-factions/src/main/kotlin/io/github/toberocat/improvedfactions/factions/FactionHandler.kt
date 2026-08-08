package io.github.toberocat.improvedfactions.factions

import io.github.toberocat.improvedfactions.messages.MessageBroker
import io.github.toberocat.improvedfactions.translation.sendLocalized
import org.bukkit.Bukkit

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
object FactionHandler {
    fun generateColor(id: Int) = Integer.parseInt(
        Integer.toHexString("${id}-${Bukkit.getServer().name}".hashCode())
            .padStart(6, '0')
            .substring(0, 6), 16
    )

    fun createListenersFor(factionId: Int) {
        MessageBroker.listenLocalized(factionId) { message ->
            val members = io.github.toberocat.improvedfactions.database.storage.StorageManager.cache
                .factionMembers(factionId).mapNotNull(Bukkit::getPlayer)
            members.forEach { it.sendLocalized(message.key, message.placeholders) }
        }
    }

}
