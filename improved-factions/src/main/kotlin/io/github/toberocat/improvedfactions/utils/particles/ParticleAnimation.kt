package io.github.toberocat.improvedfactions.utils.particles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Supplier

abstract class ParticleAnimation(protected val plugin: ImprovedFactionsPlugin,
                                 protected val animationTicks: Int,
    protected val positionSupplier: Supplier<Location>) : BukkitRunnable() {
    protected var currentTick = 0

    fun playAnimation() {
        if (BaseModule.config.hideDecorativeParticles) {
            return
        }

        runTaskTimer(plugin, 0, BaseModule.config.particleTickSpeed)
    }

    override fun run() {
        nextTick(currentTick.toDouble() / animationTicks)
        if (currentTick++ >= animationTicks) {
            cancel()
        }
    }

    abstract fun nextTick(completed: Double)
}