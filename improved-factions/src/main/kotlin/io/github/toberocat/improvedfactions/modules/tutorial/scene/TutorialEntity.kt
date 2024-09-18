package io.github.toberocat.improvedfactions.modules.tutorial.scene

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.tutorial.world.TutorialAreaPoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.patheloper.api.pathing.filter.filters.PassablePathFilter
import org.patheloper.mapping.bukkit.BukkitMapper

class TutorialEntity(
    private val targetPlayer: Player,
    val entity: Entity,
    private val plugin: ImprovedFactionsPlugin,
) {

    suspend fun moveTo(destination: TutorialAreaPoint, speed: Double = 1.0) {
        val startPosition = BukkitMapper.toPathPosition(entity.location)
        val endPosition = BukkitMapper.toPathPosition(destination.getLocation())

        val result = plugin.pathfinder.findPath(startPosition, endPosition, listOf(PassablePathFilter())).await()
        if (!result.successful()) {
            withContext(plugin.bukkitDispatcher) { targetPlayer.sendMessage("Could not find a path to the location.") }
            return
        }

        val pathLocations = result.path.map { BukkitMapper.toLocation(it) }
        val completion = CompletableDeferred<Unit>()
        withContext(plugin.bukkitDispatcher) { MovementTask(entity, pathLocations, speed, plugin, completion).start() }
        completion.await()
    }

    fun remove() {
        entity.remove()
    }

    private class MovementTask(
        private val entity: Entity,
        private val path: List<Location>,
        private val speed: Double,
        private val plugin: ImprovedFactionsPlugin,
        private val completableDeferred: CompletableDeferred<Unit>,
    ) {

        private var currentIndex = 0

        fun start() {
            object : BukkitRunnable() {
                override fun run() {
                    if (currentIndex >= path.size) {
                        completableDeferred.complete(Unit)
                        this.cancel()
                        return
                    }

                    moveEntityTowards(path[currentIndex])
                    renderPathParticles()
                }
            }.runTaskTimer(plugin, 0L, 1L)
        }

        private fun moveEntityTowards(targetLocation: Location) {
            val entityLocation = entity.location
            val direction = targetLocation.toVector().subtract(entityLocation.toVector())
            val distance = direction.length()

            if (distance < 0.1) {
                currentIndex++
            } else {
                val moveDistance = speed / 20.0
                val moveVector = direction.normalize().multiply(moveDistance)
                val nextLocation = entityLocation.add(moveVector)
                // Look at the target location
                entityLocation.direction = direction
                entity.teleport(nextLocation)
            }
        }

        private fun renderPathParticles() {
            val currentLocation = entity.location
            val remainingPath = path.subList(currentIndex, path.size)
            val maxRenderDistanceSquared = 100.0 // Render particles within 10 blocks
            val particlesPerTick = 5
            var particlesSpawned = 0

            for (location in remainingPath) {
                if (currentLocation.distanceSquared(location) <= maxRenderDistanceSquared) {
                    location.world?.spawnParticle(
                        Particle.REDSTONE,
                        location,
                        1,
                        Particle.DustOptions(Color.RED, 1f)
                    )
                    particlesSpawned++
                    if (particlesSpawned >= particlesPerTick) break
                }
            }
        }
    }
}