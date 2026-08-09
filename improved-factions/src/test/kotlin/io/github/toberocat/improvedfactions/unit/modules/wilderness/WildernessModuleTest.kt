package io.github.toberocat.improvedfactions.unit.modules.wilderness

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.wilderness.config.WildernessModuleConfig
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockbukkit.mockbukkit.world.WorldMock
import java.util.concurrent.TimeUnit
import kotlin.test.*

class WildernessModuleTest : ImprovedFactionsTest() {
    
    private lateinit var wildernessConfig: WildernessModuleConfig
    private lateinit var testWorld: WorldMock

    @BeforeEach
    override fun setUp() {
        super.setUp()
        testWorld = testWorld("wilderness_test_world")
        wildernessConfig = WildernessModuleConfig()
    }
//
//    @Test
//    fun `test random location within world bounds`() {
//        // Set up a world border
//        val worldBorder = testWorld.worldBorder
//        worldBorder.center = Location(testWorld, 0.0, 64.0, 0.0)
//        worldBorder.size = 1000.0
//        
//        // Try to get a random location
//        val startLocation = Location(testWorld, 0.0, 64.0, 0.0)
//        val randomLocation = wildernessConfig.getRandomLocation(startLocation)
//        
//        // Verify the location is valid and within world border
//        assertNotNull(randomLocation)
//        assertTrue(randomLocation.x >= -500 && randomLocation.x <= 500, "X coordinate should be within world border")
//        assertTrue(randomLocation.z >= -500 && randomLocation.z <= 500, "Z coordinate should be within world border")
//        assertEquals(testWorld, randomLocation.world, "Location should be in the same world")
//    }

//    @Test
//    fun `test teleport proximity`() {
//        // Create a config with a specific teleport proximity
//        val customConfig = WildernessModuleConfig(
//            teleportProximity = 100,
//            retryLimit = 10
//        )
//        
//        val startLocation = Location(testWorld, 0.0, 0.0, 0.0)
//        val randomLocation = customConfig.getRandomLocation(startLocation)
//        randomLocation?.y = 0.0
//        
//        // Verify the location is within the proximity range
//        assertNotNull(randomLocation)
//        val distanceSquared = startLocation.distanceSquared(randomLocation)
//        assertTrue(distanceSquared <= 100*100, 
//            "Distance squared ($distanceSquared) should be less than or equal to 10000")
//    }

//    @Test
//    fun `test custom regions`() {
//        // Create a config section for regions
//        val config = YamlConfiguration()
//        config.set("factions.wilderness.regions.test_region.min-x", 100)
//        config.set("factions.wilderness.regions.test_region.max-x", 200)
//        config.set("factions.wilderness.regions.test_region.min-z", 100)
//        config.set("factions.wilderness.regions.test_region.max-z", 200)
//        config.set("factions.wilderness.regions.test_region.world", testWorld.name)
//        
//        val customConfig = WildernessModuleConfig()
//        customConfig.reload(plugin, config)
//        
//        val startLocation = Location(testWorld, 0.0, 64.0, 0.0)
//        val randomLocation = customConfig.getRandomLocation(startLocation)
//        
//        // Verify the location is within the defined region
//        assertNotNull(randomLocation)
//        assertTrue(randomLocation.x >= 100 && randomLocation.x <= 200, 
//            "X coordinate should be within region bounds")
//        assertTrue(randomLocation.z >= 100 && randomLocation.z <= 200, 
//            "Z coordinate should be within region bounds")
//    }

    @Test
    fun `test retry limit reached with impossible conditions`() {
        // Create a config with impossible conditions
        val config = YamlConfiguration()
        
        // Set a region in a non-existent world
        config.set("factions.wilderness.regions.impossible.min-x", 10000)
        config.set("factions.wilderness.regions.impossible.max-x", 10001)
        config.set("factions.wilderness.regions.impossible.min-z", 10000)
        config.set("factions.wilderness.regions.impossible.max-z", 10001)
        config.set("factions.wilderness.regions.impossible.world", "non_existent_world")
        
        config.set("factions.wilderness.retry-limit", 3)
        
        val customConfig = WildernessModuleConfig()
        customConfig.reload(plugin, config)
        
        val startLocation = Location(testWorld, 0.0, 64.0, 0.0)
        
        // The module should fail to find a valid location after retries
        val randomLocation = customConfig.getRandomLocation(startLocation)
        assertNull(randomLocation, "Should return null when no valid location can be found")
    }

    @Test
    fun `reload honors wilderness cooldown value and unit independently`() {
        val config = YamlConfiguration().apply {
            set("factions.wilderness.cooldown-value", 17L)
            set("factions.wilderness.cooldown-unit", "MINUTES")
        }

        wildernessConfig.reload(plugin, config)

        assertEquals(17L, wildernessConfig.cooldown)
        assertEquals(TimeUnit.MINUTES, wildernessConfig.timeUnit)
    }

    @Test
    fun `world border wilderness locations stay within the configured border`() {
        testWorld.worldBorder.apply {
            center = Location(testWorld, 120.0, 64.0, -80.0)
            size = 40.0
        }
        val config = YamlConfiguration().apply {
            set("factions.wilderness.teleport-proximity", -1)
            set("factions.wilderness.retry-limit", 100)
            set("factions.wilderness.prevent-spawn-over-liquids", false)
            set("factions.wilderness.blacklisted-biomes", emptyList<String>())
            set("factions.wilderness.blacklisted-worlds", emptyList<String>())
        }
        wildernessConfig.reload(plugin, config)

        repeat(25) {
            val location = requireNotNull(wildernessConfig.getRandomLocation(Location(testWorld, 0.0, 64.0, 0.0)))

            assertEquals(testWorld, location.world)
            assertTrue(location.x in 100.0..140.0, "x=${location.x} must remain inside the world border")
            assertTrue(location.z in -100.0..-60.0, "z=${location.z} must remain inside the world border")
        }
    }

//    @Test
//    fun `test respecting blacklisted worlds`() {
//        val customConfig = WildernessModuleConfig(
//            allowedWorlds = emptySet(),
//            retryLimit = 3
//        )
//        
//        val startLocation = Location(testWorld, 0.0, 64.0, 0.0)
//        
//        // The module should fail to find a valid location in a blacklisted world
//        val randomLocation = customConfig.getRandomLocation(startLocation)
//        assertNull(randomLocation, "Should return null when the world is blacklisted")
//    }

//    @Test
//    fun `test respects faction claims`() {
//        // Create a faction and claim chunks around the center
//        val faction = testFaction(power = 1000)
//        
//        transaction {
//            for (x in -1..1) {
//                for (z in -1..1) {
//                    val chunk = testWorld.getChunkAt(x, z)
//                    faction.claim(chunk)
//                }
//            }
//        }
//        
//        val customConfig = WildernessModuleConfig(
//            teleportProximity = 50,
//            retryLimit = 10,
//        )
//        
//        // Start location at the center of claimed area
//        val startLocation = Location(testWorld, 0.0, 64.0, 0.0)
//        val randomLocation = customConfig.getRandomLocation(startLocation)
//        
//        assertNotNull(randomLocation)
//        transaction {
//            val claim = randomLocation.getFactionClaim()
//            assertNull(claim, "Random location should not be in a claimed chunk")
//        }
//    }

//    @Test
//    fun `test multiple regions configuration`() {
//        // Create a config with multiple regions
//        val config = YamlConfiguration()
//        config.set("factions.wilderness.regions.region1.min-x", 100)
//        config.set("factions.wilderness.regions.region1.max-x", 200)
//        config.set("factions.wilderness.regions.region1.min-z", 100)
//        config.set("factions.wilderness.regions.region1.max-z", 200)
//        config.set("factions.wilderness.regions.region1.world", testWorld.name)
//        
//        config.set("factions.wilderness.regions.region2.min-x", -200)
//        config.set("factions.wilderness.regions.region2.max-x", -100)
//        config.set("factions.wilderness.regions.region2.min-z", -200)
//        config.set("factions.wilderness.regions.region2.max-z", -100)
//        config.set("factions.wilderness.regions.region2.world", testWorld.name)
//        
//        val customConfig = WildernessModuleConfig()
//        customConfig.reload(plugin, config)
//        
//        // Try multiple times to verify we get locations from both regions
//        val region1Hits = mutableListOf<Location>()
//        val region2Hits = mutableListOf<Location>()
//        
//        val startLocation = Location(testWorld, 0.0, 64.0, 0.0)
//        
//        // Run multiple attempts to have a high chance of hitting both regions
//        repeat(20) {
//            val randomLocation = customConfig.getRandomLocation(startLocation) ?: return@repeat
//            
//            if (randomLocation.x in 100.0..200.0 && randomLocation.z in 100.0..200.0) {
//                region1Hits.add(randomLocation)
//            } else if (randomLocation.x in -200.0..-100.0 && randomLocation.z in -200.0..-100.0) {
//                region2Hits.add(randomLocation)
//            }
//        }
//        
//        // We should have at least one hit in each region
//        assertTrue(region1Hits.isNotEmpty(), "Should find at least one location in region 1")
//        assertTrue(region2Hits.isNotEmpty(), "Should find at least one location in region 2")
//    }
}
