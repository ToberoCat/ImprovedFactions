package io.github.toberocat.improvedfactions.modules.tutorial.scene

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.modules.tutorial.task.LookAtTask
import io.github.toberocat.improvedfactions.modules.tutorial.world.TutorialArea
import io.github.toberocat.improvedfactions.modules.tutorial.world.TutorialAreaPoint
import io.github.toberocat.improvedfactions.utils.lerp
import kotlinx.coroutines.withContext
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

abstract class Scene(
    protected val player: Player,
    protected val plugin: ImprovedFactionsPlugin,
) {
    val fakePlayerUUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    protected val tutorialEntities = mutableListOf<TutorialEntity>()
    lateinit var area: TutorialArea

    private var lookAtTask: LookAtTask? = null

    abstract suspend fun start()

    protected suspend fun createEntity(
        entityType: EntityType,
        location: TutorialAreaPoint,
        name: String,
    ) = withContext(plugin.bukkitDispatcher) {
        val entity = location.getLocation().world?.spawnEntity(location.getLocation(), entityType) ?: return@withContext null
        if (entity !is LivingEntity) {
            return@withContext null
        }

        entity.customName = name
        entity.isCustomNameVisible = true
        entity.isInvulnerable = true
        entity.setAI(false)

        val tutorialEntity = TutorialEntity(player, entity, plugin)
        tutorialEntities.add(tutorialEntity)
        return@withContext tutorialEntity
    }

    protected fun getOrCreateTutorialFaction(): Faction {
        var faction = transaction { FactionHandler.getFaction("") }
        if (faction != null) {
            return faction
        }
        faction = FactionHandler.createFaction(fakePlayerUUID, "__tutorial_faction__")
        return faction
    }

    protected fun lookAt(supplier: () -> Location?) {
        lookAtTask = LookAtTask(player, supplier)
        lookAtTask?.runTaskTimer(plugin, 0, 1)
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
        lookAtTask?.cancel()
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