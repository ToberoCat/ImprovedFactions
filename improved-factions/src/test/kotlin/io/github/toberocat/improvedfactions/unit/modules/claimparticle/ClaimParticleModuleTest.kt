package io.github.toberocat.improvedfactions.unit.modules.claimparticle

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.modules.claimparticle.ClaimParticleModule
import org.junit.jupiter.api.Test
import org.mockbukkit.mockbukkit.scheduler.RepeatingTask
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ClaimParticleModuleTest : ImprovedFactionsTest() {
    @Test
    fun `claim particles are scheduled using the configured interval`() {
        val configuredInterval = 37L
        server.scheduler.cancelTasks(plugin)
        plugin.config.set("factions.claim-particles.particle-spawn-interval", configuredInterval)

        val module = ClaimParticleModule()
        module.onEnable(plugin)
        module.reloadConfig(plugin)

        val task = module.renderParticlesTask as RepeatingTask
        assertEquals(configuredInterval, task.scheduledTick)
        assertEquals(configuredInterval, task.period)
    }

    @Test
    fun `claim particle task is cancelled when the module is disabled`() {
        val module = ClaimParticleModule()
        module.onEnable(plugin)
        val task = module.renderParticlesTask as RepeatingTask

        module.onDisable(plugin)

        assertTrue(task.isCancelled)
        kotlin.test.assertNull(module.renderParticlesTask)
    }

    @Test
    fun `particle render distance is interpreted as blocks rather than squared blocks`() {
        plugin.config.set("factions.claim-particles.block-render-distance", 17)
        val module = ClaimParticleModule()

        module.reloadConfig(plugin)

        // Rendering compares distanceSquared, so the configured block distance
        // must be squared exactly once at the configuration boundary.
        assertEquals(17 * 17, module.config.blockRenderDistance)
    }
}
