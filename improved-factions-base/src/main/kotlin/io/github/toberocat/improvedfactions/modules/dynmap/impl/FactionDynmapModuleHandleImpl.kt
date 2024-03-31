package io.github.toberocat.improvedfactions.modules.dynmap.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapModuleConfig
import io.github.toberocat.improvedfactions.modules.dynmap.handles.FactionDynmapModuleHandle
import io.github.toberocat.improvedfactions.utils.toOfflinePlayer
import io.github.toberocat.improvedfactions.zone.Zone
import org.dynmap.DynmapCommonAPI
import org.dynmap.markers.MarkerSet

class FactionDynmapModuleHandleImpl(
    private val config: DynmapModuleConfig,
    private val plugin: ImprovedFactionsPlugin,
    api: DynmapCommonAPI
) : FactionDynmapModuleHandle {
    private val set = createFactionMarker(api)
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

    override fun factionClusterChange(cluster: Cluster) {
        cluster.getReadOnlyPositions().forEach {
            val faction = FactionHandler.getFaction(cluster.factionId) ?: return@forEach
            addAreaMarker(faction.name, it) { label ->
                plugin.papiTransformer(faction.owner.toOfflinePlayer(), label)
                    .replace("%faction_name%", faction.name)
            }
        }
    }

    override fun factionClaimRemove(position: Position) {
        set.findAreaMarker(position.uniquId())?.deleteMarker()
    }

    override fun zoneClaimAdd(zone: Zone, position: Position) {
        if (!config.showZones)
            return

        addAreaMarker(zone.type, position) { it }
    }

    override fun zoneClaimRemove(position: Position) {
        set.findAreaMarker(position.uniquId())?.deleteMarker()
    }

    private fun addAreaMarker(name: String, position: Position, transformer: (input: String) -> String) {
        val worldX = position.x * 16.0
        val worldZ = position.y * 16.0
        val label = transformer(config.infoWindows[name] ?: config.infoWindows["__default__"] ?: name)
        val marker = set.createAreaMarker(
            position.uniquId(),
            label,
            true,
            position.world,
            doubleArrayOf(worldX, worldX + 16),
            doubleArrayOf(worldZ, worldZ + 16),
            false
        )
        (config.claimColors[name] ?: config.claimColors["__default__"])?.let { colorConfig ->
            marker.setFillStyle(colorConfig.opacity, colorConfig.color)
            marker.setLineStyle(0, 0.0, 0)
        }
    }
}