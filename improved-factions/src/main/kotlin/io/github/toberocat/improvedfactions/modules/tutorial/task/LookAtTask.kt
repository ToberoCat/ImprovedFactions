package io.github.toberocat.improvedfactions.modules.tutorial.task

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.utils.lerp
import kotlinx.coroutines.future.await
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.CompletableFuture
import kotlin.math.abs

class LookAtTask(
    private val targetEntity: LivingEntity,
    private val supplier: () -> Location?,
    private val future: CompletableFuture<Void> = CompletableFuture(),
) : BukkitRunnable() {
    private var lastStamp = System.currentTimeMillis()

    companion object {
        suspend fun lookAt(plugin: ImprovedFactionsPlugin,
                   targetEntity: LivingEntity,
                   supplier: () -> Location?) {
            val future = CompletableFuture<Void>()
            val task = LookAtTask(targetEntity, supplier, future)
            task.runTaskTimer(plugin, 0, 1)
            future.await()
        }
    }

    override fun run() {
        val target = supplier() ?: return

        val currentStamp = System.currentTimeMillis()
        val deltaTime = (currentStamp - lastStamp) / 1000.0
        lastStamp = currentStamp

        val playerLocation = targetEntity.location.clone()
        val currentDirection = playerLocation.direction

        val targetDirection = target.toVector().subtract(playerLocation.toVector()).normalize()

        val newDirection = currentDirection.lerp(targetDirection, 10 * deltaTime).normalize()

        playerLocation.direction = newDirection

        targetEntity.teleport(playerLocation)

        val angleDifference = currentDirection.angle(targetDirection)

        if (abs(angleDifference) < 0.05) {
            future.complete(null)
        }
    }
}