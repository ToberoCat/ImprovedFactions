package io.github.toberocat.improvedfactions.utils.particles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.bukkit.Location
import org.bukkit.Particle
import java.util.function.Supplier
import kotlin.math.cos
import kotlin.math.sin

class TeleportParticles(
    plugin: ImprovedFactionsPlugin,
    animationTicks: Int,
    positionSupplier: Supplier<Location>,
    private val helixRadius: Double = 1.0
) : ParticleAnimation(plugin, animationTicks, positionSupplier) {

    override fun nextTick(completed: Double) {
        val height = completed * 2
        val angularOffset = completed * Math.PI * 2

        val center = positionSupplier.get()

        for (i in 0..16) {
            val angle = i * Math.PI / 8 + angularOffset
            val x = center.x + helixRadius * cos(angle)
            val z = center.z + helixRadius * sin(angle)
            val location = Location(center.world, x, center.y + height, z)
            location.world!!.spawnParticle(Particle.TOTEM, location, 1, 0.0, 0.0, 0.0, 0.0)
        }
    }
}