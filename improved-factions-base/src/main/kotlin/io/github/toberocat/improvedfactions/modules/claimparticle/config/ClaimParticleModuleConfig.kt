package io.github.toberocat.improvedfactions.modules.claimparticle.config

import org.bukkit.configuration.file.FileConfiguration

data class ClaimParticleModuleConfig(
    var particleCount: Int = 2,
    var particleSpeed: Double = 0.0,
    var particleSpread: Double = 0.1,
    var blockRenderDistance: Int = 30 * 30,
    var chunkRenderDistance: Int = 1,
    var minParticleSize: Float = 0.0f,
    var maxParticleSize: Float = 2.0f,
    var particleSizeBias: Float = 0.5f,
    var particleSpawnInterval: Long = 15
) {
    private val configPath = "factions.claim-particles"

    fun reload(config: FileConfiguration) {
        particleCount = config.getInt("$configPath.particle-count", particleCount)
        particleSpeed = config.getDouble("$configPath.particle-speed", particleSpeed)
        particleSpread = config.getDouble("$configPath.particle-spread", particleSpread)
        chunkRenderDistance = config.getInt("$configPath.chunk-render-distance", chunkRenderDistance)
        minParticleSize = config.getDouble("$configPath.min-particle-size", minParticleSize.toDouble()).toFloat()
        maxParticleSize = config.getDouble("$configPath.max-particle-size", maxParticleSize.toDouble()).toFloat()
        particleSizeBias = config.getDouble("$configPath.particle-size-bias", particleSizeBias.toDouble()).toFloat()
        particleSpawnInterval = config.getLong("$configPath.particle-spawn-interval", particleSpawnInterval)

        config.getInt("$configPath.block-render-distance", blockRenderDistance).also { blockRenderDistance = it * it }
    }
}