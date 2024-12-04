package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class GeneralPvPListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace(): String = "general-pvp"

    @EventHandler
    fun pvp(event: EntityDamageByEntityEvent) = loggedTransaction {
        if (event.entity !is Player
            || event.entity.location.getFactionClaim()?.zoneType != zoneType
            || event.damager !is Player
        ) return@loggedTransaction
        protectChunk(event, event.entity, event.damager as Player)
    }
}