package io.github.toberocat.improvedfactions.modules.tutorial.manager

import io.github.toberocat.improvedfactions.modules.tutorial.world.EmptyChunkGenerator
import io.github.toberocat.improvedfactions.modules.tutorial.world.TutorialArea
import org.bukkit.*
import org.bukkit.entity.Player
import java.io.File

object TutorialWorldManager {

    private const val WORLD_NAME = "faction_tutorial_world"
    private const val CELL_SIZE = 128.0
    private const val GRID_SIZE = 32
    private val world = createWorld()
    private var currentCell = 0

    fun createPlayerArea(): TutorialArea {
        val cellX = currentCell % GRID_SIZE
        val cellZ = currentCell / GRID_SIZE
        currentCell++

        return TutorialArea(Location(world, cellX * CELL_SIZE, 64.0, cellZ * CELL_SIZE))
    }

    fun TutorialArea.deleteTutorialArea() {
        val world = origin.world
        val minX = origin.blockX - CELL_SIZE.toInt()
        val minY = origin.blockY - CELL_SIZE.toInt()
        val minZ = origin.blockZ - CELL_SIZE.toInt()

        val maxX = origin.blockX + CELL_SIZE.toInt()
        val maxY = origin.blockY + CELL_SIZE.toInt()
        val maxZ = origin.blockZ + CELL_SIZE.toInt()

        for (x in minX..maxX)
            for (y in minY..maxY)
                for (z in minZ..maxZ)
                    world?.getBlockAt(x, y, z)?.type = Material.AIR
    }

    private fun createWorld(): World = Bukkit.getWorld(WORLD_NAME) ?: run {
        WorldCreator(WORLD_NAME)
            .generator(EmptyChunkGenerator())
            .seed(0)
            .generateStructures(false)
            .createWorld()?.also {
                it.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
                it.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                it.isAutoSave = false
                Bukkit.getLogger().info("World '$WORLD_NAME' created successfully.")
            } ?: throw IllegalStateException("Failed to create temporary world '$WORLD_NAME'.")
    }
}
