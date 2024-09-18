package io.github.toberocat.improvedfactions.modules.tutorial.scene

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.modules.claimparticle.config.ClaimParticleModuleConfig
import io.github.toberocat.improvedfactions.modules.claimparticle.handles.RenderParticlesTask
import io.github.toberocat.improvedfactions.modules.tutorial.world.TutorialAreaPoint
import kotlinx.coroutines.delay
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

// https://chatgpt.com/g/g-tvLQ5jbIz-improved-factions-assistant/c/66eb340d-dafc-8003-aae0-276baf717685
class SampleScene(player: Player, plugin: ImprovedFactionsPlugin) : Scene(player, plugin) {

    override suspend fun start() {
        val claimRendererTask =
            RenderParticlesTask(ClaimParticleModuleConfig()) // ToDo: Use actual claim particle config
        val tutorialFaction = getOrCreateTutorialFaction()
        val claim = getClaim(tutorialFaction, area.point(0, 0, 0)) ?: return
        val claimCluster = transaction { claim.claimCluster } ?: return
        val taskId = plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            transaction { claimRendererTask.renderClusterParticles(player, claimCluster) }
        }, 0, 10).taskId

        area.createArea(
            area.point(0, 0, 0),
            area.point(32, 0, 16),
            Material.GRASS_BLOCK
        )

        movePlayer(8, 8, 0)
        val villager = createEntity(EntityType.VILLAGER, area.point(24, 1, 8), "Villager") ?: return
        lookAtEntity(villager)
        villager.moveTo(area.point(8, 1, 8), 1.5)
        villager.runCommand("/factions power")
        player.sendMessage("Each faction has a power that can increase or decrease based on " +
                "various activities like gaining members or losing players in combat.")
        delay(5000)

        // Clean up
        stopLookAt()
        removeEntities()
        claim.chunk()?.let { tutorialFaction.unclaim(it) }
        plugin.server.scheduler.cancelTask(taskId)
    }

    private fun getClaim(faction: Faction, point: TutorialAreaPoint) = transaction {
        val location = point.getLocation()
        if (location.getFactionClaim()?.isClaimed() == true) {
            return@transaction location.getFactionClaim()
        }
        return@transaction faction.claim(location.chunk)
    }
}