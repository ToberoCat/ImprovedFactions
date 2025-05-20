package io.github.toberocat.improvedfactions.utils

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.config.EventDisplayLocation
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.translation.LocalizationKey
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.particles.TeleportParticles
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration
import kotlin.math.ceil

class PlayerTeleporter(
    private val plugin: ImprovedFactionsPlugin,
    private val player: Player,
    private val titleKey: LocalizationKey,
    private val subtitleKey: LocalizationKey,
    private val onTeleport: () -> Unit,
    private val standStillMs: Long = 5000,
    private val playAnimation: Boolean = true,
) : BukkitRunnable() {
    private val audience = player.toAudience()
    private val startedLocation = player.location
    private val startTime = System.currentTimeMillis()
    private val teleportAnimation = TeleportParticles(
        plugin,
        (standStillMs * 0.02).toInt(),
        { player.location }
    )


    fun startTeleport() {
        audience.sendTitlePart(
            TitlePart.TIMES, Title.Times.times(
                Duration.ofMillis(0),
                Duration.ofMillis(1500),
                Duration.ofMillis(0)
            )
        )

        if (playAnimation) {
            teleportAnimation.playAnimation()
        }

        val displayLocation = BaseModule.config.territoryDisplayLocation
        if (displayLocation == EventDisplayLocation.ACTIONBAR ||
            displayLocation == EventDisplayLocation.CHAT
        ) {
            displayLocation.display(player, titleKey, null)
        }

        runTaskTimer(plugin, 0, 20)
    }

    override fun cancel() {
        runCatching {
            teleportAnimation.cancel()
        }.onFailure { plugin.logger.warning("Failed to cancel teleport animation") }
        super.cancel()
    }

    override fun run() {
        val current = System.currentTimeMillis()
        if (current - startTime >= standStillMs) {
            onTeleport()
            cancel()
            return
        }

        val leftSeconds = ceil((standStillMs - (current - startTime)) / 1000.0).toInt()
        val displayLocation = BaseModule.config.territoryDisplayLocation

        displayLocation.displayLocationHandler(
            player,
            titleKey,
            subtitleKey,
            mapOf("time" to leftSeconds.toString())
        )

        val distance = player.location.distanceSquared(startedLocation)
        if (distance > 0.01) {
            player.sendLocalized("base.player-teleport.cancel-message")
            audience.clearTitle()
            cancel()
        }
    }
}