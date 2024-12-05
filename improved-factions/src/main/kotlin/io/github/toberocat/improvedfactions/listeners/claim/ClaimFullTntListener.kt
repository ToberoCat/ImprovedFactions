package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityExplodeEvent

class ClaimFullTntListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace(): String = "all-explosions"

    @EventHandler
    fun onTntExplode(event: EntityExplodeEvent) {
        loggedTransaction {
            val iterator = event.blockList().iterator()
            while (iterator.hasNext()) {
                val block = iterator.next()

                if (!shouldProtect(block.chunk))
                    continue

                iterator.remove()
            }
        }
    }
}