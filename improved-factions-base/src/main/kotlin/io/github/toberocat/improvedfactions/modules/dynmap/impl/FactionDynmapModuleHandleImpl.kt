package io.github.toberocat.improvedfactions.modules.dynmap.impl

import io.github.toberocat.improvedfactions.claims.clustering.ClaimClusterDetector
import io.github.toberocat.improvedfactions.claims.clustering.Cluster
import io.github.toberocat.improvedfactions.modules.dynmap.config.DynmapModuleConfig
import io.github.toberocat.improvedfactions.modules.dynmap.handles.FactionDynmapModuleHandle
import org.bukkit.Chunk
import org.dynmap.DynmapCommonAPI
import org.dynmap.markers.MarkerSet

class FactionDynmapModuleHandleImpl(
    private val config: DynmapModuleConfig,
    private val api: DynmapCommonAPI,
    private val claimClusterDetector: ClaimClusterDetector
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


    override fun clusterChange(cluster: Cluster) {
        cluster.getReadOnlyPositions().forEach {
            //set.findAreaMarker(it.uniquId())?.deleteMarker()
            set.createAreaMarker(
                it.uniquId(),
                "Cluster ${cluster.factionId}",
                false,
                it.world,
                doubleArrayOf(it.x.toDouble(), it.x.toDouble()),
                doubleArrayOf(it.y.toDouble(), it.y.toDouble()),
                false
            )
        }
    }
}