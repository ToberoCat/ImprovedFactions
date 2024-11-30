package io.github.toberocat.improvedfactions.modules.wilderness.config

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.config.ImprovedFactionsConfig
import io.github.toberocat.improvedfactions.config.PluginConfig
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import java.lang.Math.round
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin

class WildernessModuleConfig(
    var cooldown: Long = 30,
    var timeUnit: TimeUnit = TimeUnit.SECONDS,
    private var includedZones: List<String> = listOf("unmanaged"),
    var gainResistance: Boolean = true,
    var resistanceDuration: Int = 10,
    var resistanceAmplifier: Int = 5,
    private var preventSpawnOverLiquids: Boolean = true,
    var standStillUnit: TimeUnit = TimeUnit.SECONDS,
    var standStillValue: Long = 5,
    private var blacklistedBiomes: Set<String> = setOf("OCEAN"),
    var usageLimit: Int = -1,
    private var claimDistanceCheck: Int = 5,
    private var retryLimit: Int = 10,
    private var teleportProximity: Int = 100,
    private var allowedWorlds: Set<String> = emptySet(),
    private var regions: Map<String, Region> = emptyMap()
) : PluginConfig() {
    private lateinit var pluginConfig: ImprovedFactionsConfig
    private val configPath = "factions.wilderness"

    override fun reload(plugin: ImprovedFactionsPlugin, config: FileConfiguration) {
        pluginConfig = BaseModule.config

        cooldown = config.getLong("$configPath.cooldown-value", cooldown)
        timeUnit = TimeUnit.valueOf(config.getString("$configPath.cooldown-unit", timeUnit.name)!!)
        includedZones = config.getStringList("$configPath.included-zones")
        gainResistance = config.getBoolean("$configPath.gain-resistance", gainResistance)
        preventSpawnOverLiquids = config.getBoolean("$configPath.prevent-spawn-over-liquids", preventSpawnOverLiquids)
        standStillUnit = TimeUnit.valueOf(config.getString("$configPath.stand-still-unit", standStillUnit.name)!!)
        standStillValue = config.getLong("$configPath.stand-still-value", standStillValue)
        blacklistedBiomes = config.getStringList("$configPath.blacklisted-biomes").toSet()
        usageLimit = config.getInt("$configPath.usage-limit", usageLimit)
        claimDistanceCheck = config.getInt("$configPath.claim-distance-check", claimDistanceCheck)
        resistanceDuration = config.getInt("$configPath.resistance-duration", resistanceDuration)
        resistanceAmplifier = config.getInt("$configPath.resistance-amplifier", resistanceAmplifier)
        retryLimit = config.getInt("$configPath.retry-limit", retryLimit)
        teleportProximity = config.getInt("$configPath.teleport-proximity", teleportProximity)
        allowedWorlds = computeAllowedWorlds(
            pluginConfig.allowedWorlds,
            config.getStringList("$configPath.blacklisted-worlds").toSet()
        )

        val regionSection = config.getConfigurationSection("$configPath.regions") ?: return
        regions = regionSection.getKeys(false).associateWith { key ->
            Region().apply {
                minX = regionSection.getInt("$key.min-x")
                maxX = regionSection.getInt("$key.max-x")
                minZ = regionSection.getInt("$key.min-z")
                maxZ = regionSection.getInt("$key.max-z")
                world = regionSection.getString("$key.world") ?: ""
            }
        }
    }

    fun getRandomLocation(location: Location): Location? {
        for (i in 0 until retryLimit) {
            val randomLocation = chooseRandomLocation(location) ?: continue
            if (isLocationValid(randomLocation)) {
                return randomLocation
            }
        }
        return null
    }

    private fun isLocationValid(location: Location): Boolean {
        if (preventSpawnOverLiquids && location.block.isLiquid) {
            return false
        }
        if (blacklistedBiomes.contains(location.block.biome.name)) {
            return false
        }
        if (!pluginConfig.allowedWorlds.contains(location.world?.name)) {
            return false
        }

        for (i in -claimDistanceCheck until claimDistanceCheck) {
            for (j in -claimDistanceCheck until claimDistanceCheck) {
                val claim =
                    loggedTransaction { location.clone().add(i.toDouble(), 0.0, j.toDouble()).getFactionClaim() }
                        ?: continue
                val factionId = claim.factionId
                val zoneId = claim.zoneType
                if (factionId != noFactionId || zoneId !in includedZones) {
                    return false
                }
            }
        }

        return true
    }

    private fun randomValue(min: Int, max: Int) = (min..max).random()

    private fun chooseRandomLocation(location: Location): Location? {
        val x: Int
        val z: Int
        val world: World
        when {
            regions.isNotEmpty() -> {
                val region = regions.values.random()
                world = Bukkit.getWorld(region.world) ?: return null
                x = randomValue(region.minX, region.maxX)
                z = randomValue(region.minZ, region.maxZ)
            }

            teleportProximity >= 0 -> {
                world = location.world ?: return null
                val radius = teleportProximity
                val randomAngle = Math.random() * 2 * Math.PI
                x = (location.x + radius * cos(randomAngle)).toInt()
                z = (location.z + radius * sin(randomAngle)).toInt()
            }

            else -> {
                world = location.world ?: return null
                val worldBorder = world.worldBorder
                val minX: Int = round(worldBorder.center.x - worldBorder.size / 2).toInt()
                val maxX: Int = round(worldBorder.center.x + worldBorder.size / 2).toInt()
                val minZ: Int = round(worldBorder.center.z - worldBorder.size / 2).toInt()
                val maxZ: Int = round(worldBorder.center.z + worldBorder.size / 2).toInt()

                x = (minX..maxX).random()
                z = (minZ..maxZ).random()
            }
        }
        val y = world.getHighestBlockYAt(x, z)
        return Location(location.world, x.toDouble(), y.toDouble(), z.toDouble())
    }
}