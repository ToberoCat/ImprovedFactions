package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.AbstractVillager
import org.bukkit.entity.Animals
import org.bukkit.entity.Boss
import org.bukkit.entity.Golem
import org.bukkit.entity.Monster

class ClaimProtectionListener(
    private val plugin: ImprovedFactionsPlugin,
    zoneType: String,
    private val section: ConfigurationSection
) {

    init {
        register(ClaimBlockPlaceListener(zoneType))
        register(ClaimBlockBreakListener(zoneType))
        register(ClaimBlockInteractListener(zoneType))
        register(ClaimEntityInteractionListener(zoneType))
        register(ClaimEntityBreakHangingListener(zoneType))
        register(ClaimEntitySpawnedByEggListener(zoneType))
        register(ClaimVehicleBreakListener(zoneType))
        register(ClaimTntListener(zoneType))
        register(ClaimBucketListener(zoneType))
        register(ClaimTramplingListener(zoneType))
        register(ClaimEndCrystalListener(zoneType))

        register(GeneralPvPListener(zoneType))
        register(InFactionPvPListener(zoneType))

        register(ClaimPlayerDamageEntityListener("animal", Animals::class, zoneType))
        register(ClaimPlayerDamageEntityListener("villager", AbstractVillager::class, zoneType))
        register(ClaimPlayerDamageEntityListener("golem", Golem::class, zoneType))
        register(ClaimPlayerDamageEntityListener("monster", Monster::class, zoneType))
        register(ClaimPlayerDamageEntityListener("boss", Boss::class, zoneType))

        register(ClaimEntityDamagePlayerListener("golem", Golem::class, zoneType))
        register(ClaimEntityDamagePlayerListener("monster", Monster::class, zoneType))
        register(ClaimEntityDamagePlayerListener("boss", Boss::class, zoneType))
    }

    private fun register(listener: ProtectionListener) {
        if (!section.getBoolean("protection.${listener.namespace()}", true)) return

        plugin.logger.fine("Enabling claim protection for ${listener.namespace()}")
        plugin.server.pluginManager.registerEvents(listener, plugin)
    }
}