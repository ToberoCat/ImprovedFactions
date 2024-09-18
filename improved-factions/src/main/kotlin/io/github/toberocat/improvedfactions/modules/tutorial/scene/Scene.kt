package io.github.toberocat.improvedfactions.modules.tutorial.scene

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.tutorial.world.TutorialArea
import io.github.toberocat.improvedfactions.modules.tutorial.world.TutorialAreaPoint
import io.github.toberocat.improvedfactions.utils.lerp
import kotlinx.coroutines.withContext
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.math.atan2
import kotlin.math.sqrt

abstract class Scene(
    protected val player: Player,
    protected val plugin: ImprovedFactionsPlugin,
) {

    protected val tutorialEntities = mutableListOf<TutorialEntity>()
    lateinit var area: TutorialArea

    private var lookAtTask: Int? = null

    abstract suspend fun start()

    protected suspend fun createEntity(
        entityType: EntityType,
        location: TutorialAreaPoint,
        name: String? = null,
    ) = withContext(plugin.bukkitDispatcher) {
        val entity = location.getLocation().world?.spawnEntity(location.getLocation(), entityType) ?: return@withContext null
        if (name != null && entity is LivingEntity) {
            entity.customName = name
            entity.isCustomNameVisible = true
            entity.isInvulnerable = true
            entity.setAI(false)
        }
        val tutorialEntity = TutorialEntity(player, entity, plugin)
        tutorialEntities.add(tutorialEntity)
        return@withContext tutorialEntity
    }

    protected fun lookAt(supplier: () -> Location?) {
        var lastStamp = System.currentTimeMillis()
        lookAtTask = plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            val target = supplier() ?: return@Runnable

            val currentStamp = System.currentTimeMillis()
            val deltaTime = (currentStamp - lastStamp) / 1000.0
            lastStamp = currentStamp

            val playerLocation = player.location.clone()
            val currentDirection = playerLocation.direction

            val targetDirection = target.toVector().subtract(playerLocation.toVector()).normalize()

            val newDirection = currentDirection.lerp(targetDirection, 10 * deltaTime).normalize()

            playerLocation.direction = newDirection

            player.teleport(playerLocation)
        }, 0, 1).taskId
    }

    protected fun lookAtEntity(entity: TutorialEntity) {
        lookAt {
            when {
                !entity.entity.isValid -> return@lookAt null
                else -> entity.entity.location
            }
        }
    }

    protected fun stopLookAt() {
        lookAtTask?.let { plugin.server.scheduler.cancelTask(it) }
    }

    protected suspend fun movePlayer(relativeX: Int, relativeY: Int, relativeZ: Int) {
        withContext(plugin.bukkitDispatcher) {
            player.teleport(area.point(relativeX, relativeY, relativeZ).getLocation())
        }
    }

    protected fun removeEntities() {
        tutorialEntities.forEach { it.remove() }
        tutorialEntities.clear()
    }
}