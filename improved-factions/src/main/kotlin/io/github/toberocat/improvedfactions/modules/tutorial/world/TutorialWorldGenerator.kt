package io.github.toberocat.improvedfactions.modules.tutorial.world
import org.bukkit.block.Biome
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo


class EmptyChunkGenerator : ChunkGenerator() {

    override fun getDefaultBiomeProvider(worldInfo: WorldInfo) = VoidBiomeProvider()


    class VoidBiomeProvider : BiomeProvider() {
        override fun getBiome(worldInfo: WorldInfo, x: Int, y: Int, z: Int) = Biome.THE_VOID

        override fun getBiomes(worldInfo: WorldInfo) = listOf(Biome.THE_VOID)
    }
}
