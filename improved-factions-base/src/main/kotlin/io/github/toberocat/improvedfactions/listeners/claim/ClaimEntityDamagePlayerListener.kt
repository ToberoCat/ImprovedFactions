package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.reflect.KClass

class ClaimEntityDamagePlayerListener<T : Entity>(private val name: String, private val clazz: KClass<T>, zoneType: String) :
    ProtectionListener(zoneType) {

    override fun namespace(): String = "$name-damage-player"

    @EventHandler
    fun entityDamage(event: EntityDamageByEntityEvent) {
        if (!clazz.isInstance(event.damager)) return
        protectChunk(event, event.entity, (event.entity as? Player) ?: return)
    }
}