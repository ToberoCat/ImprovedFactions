package io.github.toberocat.improvedfactions.unit.config

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.config.PluginConfig
import io.github.toberocat.improvedfactions.config.ImprovedFactionsConfig
import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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

    @Test
    fun `reload fails when default faction ranks are missing`() {
        val failure = assertFailsWith<IllegalArgumentException> {
            ImprovedFactionsConfig().reload(plugin, YamlConfiguration())
        }

        assertEquals("Missing required config section: factions.default-faction-ranks", failure.message)
    }


    class TestPluginConfig : PluginConfig() {
        override fun reload(plugin: ImprovedFactionsPlugin, config: FileConfiguration) {
            // Do nothing
        }

        fun compute(allowed: Set<String>, disallowed: Set<String>) = computeAllowedWorlds(allowed, disallowed)
    }
}
