package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityExplodeEvent


class ClaimTntListener(zoneType: String) : ProtectionListener(zoneType) {


    override fun namespace(): String = "outside-explosions"


    @EventHandler
    fun onTntExplode(event: EntityExplodeEvent) {
        loggedTransaction {
            val sourceFaction = event.entity.location.getFactionClaim()?.factionId ?: noFactionId

            val iterator = event.blockList().iterator()
            while (iterator.hasNext()) {
                val block = iterator.next()
                val claim = block.getFactionClaim() ?: continue
                if (claim.zoneType != zoneType) continue
                if (claim.factionId != sourceFaction) iterator.remove()
            }
        }
    }
}