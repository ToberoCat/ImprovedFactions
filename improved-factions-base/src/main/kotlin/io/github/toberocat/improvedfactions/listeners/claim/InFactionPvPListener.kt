package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.jetbrains.exposed.sql.transactions.transaction

class InFactionPvPListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace(): String = "in-faction-pvp"

    @EventHandler
    fun pvp(event: EntityDamageByEntityEvent) = transaction {
        val damaged = event.entity as? Player
        val damager = event.damager as? Player
        if (damaged == null || damager == null || damaged.location.getFactionClaim()?.zoneType != zoneType)
            return@transaction
        if (damager.factionUser().factionId != damaged.factionUser().factionId)
            return@transaction
        event.isCancelled = true
    }
}