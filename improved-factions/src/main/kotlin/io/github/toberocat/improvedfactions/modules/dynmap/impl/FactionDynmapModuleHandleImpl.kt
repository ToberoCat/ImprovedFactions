package io.github.toberocat.improvedfactions.modules.dynmap.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapColorConfig
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapModuleConfig
import io.github.toberocat.improvedfactions.modules.dynmap.handles.FactionDynmapModuleHandle
import org.bukkit.Location
import org.dynmap.DynmapCommonAPI
import org.dynmap.markers.MarkerSet

class FactionDynmapModuleHandleImpl(
    private val config: DynmapModuleConfig,
    private val plugin: ImprovedFactionsPlugin,
    api: DynmapCommonAPI
) : FactionDynmapModuleHandle {
    private val set = createFactionMarker(api)
    private val homeIcon = api.markerAPI.getMarkerIcon("faction_home_icon") ?: api.markerAPI.createMarkerIcon(
        "faction_home_icon",
        "Faction Home",
        plugin.getResource("icons/home-icon.png")
    )

    private fun createFactionMarker(api: DynmapCommonAPI): MarkerSet {
        val markerApi = api.markerAPI

        return (markerApi.getMarkerSet(config.markerSetId) ?: markerApi.createMarkerSet(
            config.markerSetId,
            config.markerSetDisplayName,
            null,
            false
        )).also {
            it.markerSetLabel = config.markerSetDisplayName
            it.layerPriority = config.markerSetPriority
            it.hideByDefault = config.markerSetHiddenByDefault
        }
    }

    init {
        set.markers.forEach { it.deleteMarker() }
    }

    override fun renderSnapshot() {
        val snapshot = StorageManager.cache.snapshot() ?: return
        set.markers.toList().forEach { it.deleteMarker() }
        set.areaMarkers.toList().forEach { it.deleteMarker() }
        set.polyLineMarkers.toList().forEach { it.deleteMarker() }
        snapshot.homes.values.forEach { home ->
            val faction = snapshot.factions[home.factionId] ?: return@forEach
            val world = org.bukkit.Bukkit.getWorld(home.world) ?: return@forEach
            addSnapshotHome(faction, Location(world, home.x, home.y, home.z))
        }
        snapshot.claims.values.forEach { claim ->
            val faction = snapshot.factions[claim.factionId]
            val zone = io.github.toberocat.improvedfactions.zone.ZoneHandler.getZone(claim.zoneType)
            val name = faction?.name ?: zone?.type ?: return@forEach
            val color = faction?.let { FactionHandler.generateColor(it.id) }
            addAreaMarker(name, claim, color) { it }
        }
    }

    private fun getColor(name: String, overrideColor: Int? = null): DynmapColorConfig? {
        val colorPack = when {
            config.colorFactionClaims -> overrideColor?.let { DynmapColorConfig(it, 0.3) }
            else -> null
        }
        return config.claimColors[name] ?: colorPack ?: config.claimColors["__default__"]
    }

    private fun addAreaMarker(
        name: String,
        position: ClaimSnapshot,
        color: Int? = null,
        transformer: (input: String) -> String,
    ): String {
        val worldX = position.key.chunkX * 16.0
        val worldZ = position.key.chunkZ * 16.0
        val label = transformer(config.infoWindows[name] ?: config.infoWindows["__default__"] ?: name)
        val markerId = "${position.key.world}:${position.key.chunkX}:${position.key.chunkZ}"
        val marker = set.findAreaMarker(markerId) ?: set.createAreaMarker(
            markerId, label, true, position.key.world,
            doubleArrayOf(worldX, worldX + 16), doubleArrayOf(worldZ, worldZ + 16), false
        ) ?: return markerId
        getColor(name, color)?.let { marker.setFillStyle(it.opacity, it.color); marker.setLineStyle(0, 0.0, it.color) }
        return markerId
    }

    private fun addSnapshotHome(faction: FactionSnapshot, homeLocation: Location) {
        val markerId = "home_${faction.id}"
        set.findMarker(markerId)?.deleteMarker()
        set.createMarker(markerId, "${faction.name}'s Home", false, homeLocation.world!!.name,
            homeLocation.x, homeLocation.y, homeLocation.z, homeIcon, true)
    }

}
