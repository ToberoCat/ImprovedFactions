package io.github.toberocat.improvedfactions.unit.config

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.config.PluginConfig
import io.github.toberocat.improvedfactions.unit.ImprovedFactionsTest
import org.bukkit.configuration.file.FileConfiguration
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PluginConfigTest : ImprovedFactionsTest() {

    @Test
    fun `test allowed world computation`() {
        val config = TestPluginConfig()
        assertEquals(
            config.compute(
                setOf("world"),
                setOf("world_nether", "world_the_end")
            ), setOf("world")
        )
    }

    @Test
    fun `test empty world computation`() {
        val config = TestPluginConfig()
        assertEquals(
            config.compute(
                setOf(),
                setOf("world_nether", "world_the_end")
            ), setOf()
        )
    }

    @Test
    fun `test multiple worlds computation`() {
        val config = TestPluginConfig()
        assertEquals(
            config.compute(
                setOf("world", "world_nether", "world_the_end"),
                setOf("world_nether", "world_the_end")
            ), setOf("world")
        )
    }


    class TestPluginConfig : PluginConfig() {
        override fun reload(plugin: ImprovedFactionsPlugin, config: FileConfiguration) {
            // Do nothing
        }

        fun compute(allowed: Set<String>, disallowed: Set<String>) = computeAllowedWorlds(allowed, disallowed)
    }
}