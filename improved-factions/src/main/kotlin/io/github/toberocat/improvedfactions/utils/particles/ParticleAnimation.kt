package io.github.toberocat.improvedfactions.utils.particles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Supplier

abstract class ParticleAnimation(protected val plugin: ImprovedFactionsPlugin,
                                 protected val animationTicks: Int,
    protected val positionSupplier: Supplier<Location>) : BukkitRunnable() {
    protected var currentTick = 0

    fun playAnimation() {
        if (hideDecorativeParticles) {
            return
        }

        runTaskTimer(plugin, 0, tickSpeed)
    }

    override fun run() {
        nextTick(currentTick.toDouble() / animationTicks)
        if (currentTick++ >= animationTicks) {
            cancel()
        }
    }

    abstract fun nextTick(completed: Double)

    companion object {
        var hideDecorativeParticles: Boolean = false
        var tickSpeed: Long = 1
    }
}