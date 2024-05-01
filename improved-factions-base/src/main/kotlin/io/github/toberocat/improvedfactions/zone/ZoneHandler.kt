package io.github.toberocat.improvedfactions.zone

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.listeners.claim.ClaimProtectionListener
import io.github.toberocat.improvedfactions.modules.dynmap.DynmapModule
import org.bukkit.Chunk
import org.bukkit.configuration.ConfigurationSection
import org.jetbrains.exposed.sql.transactions.transaction

object ZoneHandler {
    const val FACTION_ZONE_TYPE = "default"
    private val knownZones: MutableMap<String, Zone> = mutableMapOf()

    fun createZone(
        plugin: ImprovedFactionsPlugin, type: String, section: ConfigurationSection
    ) {
        if (type.length >= FactionClaims.MAX_ZONE_NAME_LENGTH) {
            plugin.logger.warning("The zone name $type exceeds the maximum allowed zone name length. It's set to allow a maximum of ${FactionClaims.MAX_ZONE_NAME_LENGTH} characters")
            return
        }
        val announceTitle = section.getBoolean("announce-title", true)
        val allowClaiming = section.getBoolean("allow-claiming", true)
        val alwaysProtect = section.getBoolean("always-protect", false)
        val mapColor = Integer.parseInt(section.getString("map-color") ?: "AAAAAA", 16)
        val noFactionTitle = section.getString("no-faction-title") ?: "base.zone.wilderness"
        ClaimProtectionListener(plugin, type, section)

        knownZones[type] = Zone(type, noFactionTitle, announceTitle, alwaysProtect, mapColor, allowClaiming)
    }

    fun defaultZoneCheck(plugin: ImprovedFactionsPlugin) {
        if (knownZones.contains(FACTION_ZONE_TYPE)) return
        plugin.logger.severe(
            "The plugin wasn't able to find a default zone. " +
                    "This zone is essential for the plugin to function properly"
        )
    }

    fun hasZone(type: String): Boolean = knownZones.contains(type)

    fun getZone(type: String): Zone? = knownZones[type]

    fun getZones(): Set<String> = knownZones.keys

    fun getZoneClaims(): List<FactionClaim> = transaction { FactionClaim
        .find { FactionClaims.zoneType neq FACTION_ZONE_TYPE }
        .toList() }
    fun unclaim(chunk: Chunk) {
        chunk.getFactionClaim()?.let {
            it.zoneType = FACTION_ZONE_TYPE
            DynmapModule.dynmapModule().dynmapModuleHandle.zoneClaimRemove(it.toPosition())
        }
    }
}