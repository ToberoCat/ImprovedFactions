package io.github.toberocat.improvedfactions.modules.dynmap.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.claims.clustering.WorldPosition
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapColorConfig
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapModuleConfig
import io.github.toberocat.improvedfactions.modules.dynmap.handles.FactionDynmapModuleHandle
import io.github.toberocat.improvedfactions.utils.toOfflinePlayer
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import org.bukkit.Location
import org.dynmap.DynmapCommonAPI
import org.dynmap.markers.MarkerIcon
import org.dynmap.markers.MarkerSet
import java.util.UUID

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
        ZoneHandler.getZoneClaims().forEach { zoneClaimAdd(it.zoneType, it.toPosition()) }
    }

    override fun factionHomeChange(faction: Faction, homeLocation: Location) {
        val markerId = "home_${faction.id.value}"
        set.findMarker(markerId)?.deleteMarker()
        set.createMarker(
            markerId,
            "${faction.name}'s Home",
            false,
            homeLocation.world!!.name,
            homeLocation.x,
            homeLocation.y,
            homeLocation.z,
            homeIcon,
            true
        )
    }

    override fun factionClusterChange(cluster: Cluster) {
        val faction = FactionHandler.getFaction(cluster.factionId) ?: return
        addPolylineMarker(
            faction.name,
            cluster.id,
            cluster.getOuterNodes(),
            faction.generateColor()
        )
        cluster.getReadOnlyPositions().forEach {
            addAreaMarker(faction.name, it, faction.generateColor()) { label ->
                plugin.papiTransformer(faction.owner.toOfflinePlayer(), label)
                    .replace("%faction_name%", faction.name)
            }
        }
    }

    override fun factionClaimRemove(position: Position) {
        set.findAreaMarker(position.uniquId())?.deleteMarker()
    }

    override fun zoneClaimAdd(type: String, position: Position) {
        if (!config.showZones)
            return

        addAreaMarker(type, position) { it }
    }

    override fun zoneClaimRemove(position: Position) {
        set.findAreaMarker(position.uniquId())?.deleteMarker()
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
        position: Position,
        color: Int? = null,
        transformer: (input: String) -> String,
    ) {
        val worldX = position.x * 16.0
        val worldZ = position.y * 16.0
        val label = transformer(config.infoWindows[name] ?: config.infoWindows["__default__"] ?: name)
        val markerId = position.uniquId()
        val marker = set.findAreaMarker(markerId) ?: set.createAreaMarker(
            markerId,
            label,
            true,
            position.world,
            doubleArrayOf(worldX, worldX + 16),
            doubleArrayOf(worldZ, worldZ + 16),
            false
        ) ?: return


        getColor(name, color)?.let { colorConfig ->
            marker.setFillStyle(colorConfig.opacity, colorConfig.color)
            marker.setLineStyle(0, 0.0, colorConfig.color)
        }
    }

    private fun addPolylineMarker(
        name: String,
        clusterId: UUID,
        position: MutableList<WorldPosition>,
        overrideColor: Int?= null
    ) {
        position.add(position.first()) // Close the polygon
        println(position)
        val markerId = clusterId.toString()
        val xArray = position.map { it.x.toDouble() }.toDoubleArray()
        val yArray = position.map { 0.0 }.toDoubleArray()
        val zArray = position.map { it.y.toDouble() }.toDoubleArray()
        val marker = set.findPolyLineMarker(markerId) ?: set.createPolyLineMarker(
            markerId,
            "",
            false,
            position.first().world,
            DoubleArray(0),
            DoubleArray(0),
            DoubleArray(0),
            false
        ) ?: return

        marker.setCornerLocations(
            xArray,
            yArray,
            zArray
        )
        getColor(name, overrideColor)?.let {
            marker.setLineStyle(3, it.opacity + 0.2, it.color)
        }
    }
}