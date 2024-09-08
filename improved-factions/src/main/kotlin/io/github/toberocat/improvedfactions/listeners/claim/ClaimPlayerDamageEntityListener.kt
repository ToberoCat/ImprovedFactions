package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.reflect.KClass

class ClaimPlayerDamageEntityListener<T : Entity>(private val name: String, private val clazz: KClass<T>, zoneType: String) :
    ProtectionListener(zoneType) {

    override fun namespace(): String = "player-damage-$name"

    @EventHandler
    fun entityDamage(event: EntityDamageByEntityEvent) {
        if (!clazz.isInstance(event.entity)) return
        protectChunk(event, event.entity, event.damager as? Player ?: return)
    }
}