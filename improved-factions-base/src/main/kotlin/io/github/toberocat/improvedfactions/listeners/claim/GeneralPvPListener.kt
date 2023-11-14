package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.claims.getFactionClaim
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.jetbrains.exposed.sql.transactions.transaction

class GeneralPvPListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace(): String = "general-pvp"

    @EventHandler
    fun pvp(event: EntityDamageByEntityEvent) = transaction {
        if (event.entity !is Player
            || event.entity.location.getFactionClaim()?.zoneType != zoneType
            || event.damager !is Player
        ) return@transaction
        event.isCancelled = true
    }
}