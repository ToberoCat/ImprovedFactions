package io.github.toberocat.improvedfactions.modules.dynmap.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.*
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapColorConfig
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapModuleConfig
import io.github.toberocat.improvedfactions.modules.dynmap.handles.FactionDynmapModuleHandle
import io.github.toberocat.improvedfactions.utils.toOfflinePlayer
import org.bukkit.Location
import org.dynmap.DynmapCommonAPI
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

    private val clusterPolylineMarkers = mutableMapOf<UUID, MutableSet<String>>()

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

    override fun clusterChange(cluster: Cluster) {
        if (cluster is ZoneCluster && !config.showZones)
                return

        var generatedColor: Int? = null
        var name = "Unknown"
        var labelTransformer: (input: String) -> String = { it }
        when (cluster) {
            is FactionCluster -> FactionHandler.getFaction(cluster.factionId)?.run {
                generatedColor = generateColor()
                name = this.name
                labelTransformer = {
                    plugin.papiTransformer(owner.toOfflinePlayer(), it)
                        .replace("%faction_name%", name)
                }
            }

            is ZoneCluster -> name = cluster.zoneType
            else -> throw IllegalArgumentException("Unknown cluster type")
        }

        clusterPolylineMarkers[cluster.id]?.forEach { set.findPolyLineMarker(it)?.deleteMarker() }
        cluster.getOuterNodes().forEachIndexed { index, worldPositions ->
            val markerId = "${cluster.id}-$index"
            clusterPolylineMarkers.computeIfAbsent(cluster.id) { mutableSetOf() }.add(markerId)
            addPolylineMarker(
                name,
                markerId,
                worldPositions.toMutableList(),
                generatedColor
            )
        }

        //cluster.getReadOnlyPositions().forEach { addAreaMarker(name, it, generatedColor, labelTransformer) }
    }

    override fun clusterRemove(cluster: Cluster) {
        clusterPolylineMarkers[cluster.id]?.forEach { set.findPolyLineMarker(it)?.deleteMarker() }
        clusterPolylineMarkers.remove(cluster.id)
    }

    override fun removePosition(position: ChunkPosition) {
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
        position: ChunkPosition,
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
        markerId: String,
        position: MutableList<WorldPosition>,
        overrideColor: Int? = null
    ) {
        position.add(position.first()) // Close the polygon

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