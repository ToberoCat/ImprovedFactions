package io.github.toberocat.improvedfactions.modules.claimparticle.handles

import io.github.toberocat.improvedfactions.claims.clustering.WorldPosition
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World

object LineHandler {
    // ToDo: Update cache on block place / break instead of each render phase
    private val locationCache = mutableMapOf<Pair<WorldPosition, WorldPosition>, List<Location>>()

    fun clearCache() {
        locationCache.clear()
    }

    fun getLocations(start: WorldPosition, end: WorldPosition): List<Location> {
        val key = Pair(start, end)
        if (locationCache.containsKey(key)) {
            return locationCache[key]!!
        }

        val locations = computeLocations(start, end)
        locationCache[key] = locations
        return locations
    }

    private fun computeLocations(start: WorldPosition, end: WorldPosition): List<Location> {
        val locations = mutableListOf<Location>()

        val world = Bukkit.getWorld(start.world) ?: return locations
        if (start.x == end.x) {
            val minY = minOf(start.y, end.y)
            val maxY = maxOf(start.y, end.y)
            for (y in minY..maxY) {
                locations.addAll(getLocationsAt(start.x, y, world))
            }
        } else if (start.y == end.y) {
            val minX = minOf(start.x, end.x)
            val maxX = maxOf(start.x, end.x)
            for (x in minX..maxX) {
                locations.addAll(getLocationsAt(x, start.y, world))
            }
        }

        return locations
    }

    private fun getLocationsAt(x: Int, y: Int, world: World): List<Location> {
        val upperHeight: Int = world.getHighestBlockAt(x, y).y + 1
        val lowerHeight = world.minHeight
        val locations = mutableListOf<Location>()

        for (i in lowerHeight..upperHeight) {
            if (world.getBlockAt(x, i, y).type == Material.AIR && world.getBlockAt(x, i - 1, y).type != Material.AIR) {
                locations.add(Location(world, x.toDouble(), i.toDouble() + 0.5, y.toDouble()))
            }
        }

        return locations
    }
}