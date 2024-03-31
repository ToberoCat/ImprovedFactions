package io.github.toberocat.improvedfactions.modules.dynmap.impl

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.claims.clustering.Position
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapModuleConfig
import io.github.toberocat.improvedfactions.modules.dynmap.handles.FactionDynmapModuleHandle
import io.github.toberocat.improvedfactions.utils.toOfflinePlayer
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
            val worldX = it.x * 16.0
            val worldZ = it.y * 16.0
            val faction = FactionHandler.getFaction(cluster.factionId) ?: return@forEach
            var label = config.infoWindows[faction.name] ?: config.infoWindows["__default__"] ?: "Faction: ${faction.name}"
            label = plugin.papiTransformer(faction.owner.toOfflinePlayer(), label)
            label = label.replace("%faction_name%", faction.name)
            set.createAreaMarker(
                it.uniquId(),
                label,
                true,
                it.world,
                doubleArrayOf(worldX, worldX + 16),
                doubleArrayOf(worldZ, worldZ + 16),
                false
            )
        }
    }

    override fun factionClaimRemove(position: Position) {
        set.findAreaMarker(position.uniquId())?.deleteMarker()
    }
}