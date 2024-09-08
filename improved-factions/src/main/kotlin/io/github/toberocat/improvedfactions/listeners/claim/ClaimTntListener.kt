package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityExplodeEvent


class ClaimTntListener(zoneType: String) : ProtectionListener(zoneType) {


    override fun namespace(): String = "explosions"


    @EventHandler
    fun onTNTExplode(event: EntityExplodeEvent) {
        loggedTransaction {
            val sourceFaction = event.entity.location.getFactionClaim()?.factionId ?: noFactionId

            val iterator = event.blockList().iterator()
            while (iterator.hasNext()) {
                val block = iterator.next()
                val targetFaction = block.getFactionClaim()?.factionId ?: continue
                if (targetFaction != sourceFaction) iterator.remove()
            }
        }
    }
}