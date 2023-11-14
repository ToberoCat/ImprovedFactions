package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.AbstractVillager
import org.bukkit.entity.Animals
import org.bukkit.entity.Boss
import org.bukkit.entity.Golem
import org.bukkit.entity.Monster

class ClaimProtectionListener(private val plugin: ImprovedFactionsPlugin,
                              private val zoneType: String,
                              private val section: ConfigurationSection) {

    init {
        register(ClaimBlockPlaceListener(zoneType))
        register(ClaimBlockBreakListener(zoneType))
        register(ClaimBlockInteractListener(zoneType))
        register(ClaimEntityInteractionListener(zoneType))
        register(ClaimEntityBreakHangingListener(zoneType))
        register(ClaimEntitySpawnedByEggListener(zoneType))
        register(ClaimVehicleBreakListener(zoneType))
        register(ClaimTntListener(zoneType))

        register(GeneralPvPListener(zoneType))
        register(InFactionPvPListener(zoneType))

        register(ClaimEntityDamageListener("animal", Animals::class, zoneType))
        register(ClaimEntityDamageListener("villager", AbstractVillager::class, zoneType))
        register(ClaimEntityDamageListener("golem", Golem::class, zoneType))
        register(ClaimEntityDamageListener("monster", Monster::class, zoneType))
        register(ClaimEntityDamageListener("boss", Boss::class, zoneType))
    }

    private fun register(listener: ProtectionListener) {
        if (!section.getBoolean("protection.${listener.namespace()}", true)) return

        plugin.logger.fine("Enabling claim protection for ${listener.namespace()}")
        plugin.server.pluginManager.registerEvents(listener, plugin)
    }
}