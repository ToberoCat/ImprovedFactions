package io.github.toberocat.improvedfactions.listeners.move

import io.github.toberocat.improvedfactions.translation.TranslatedBossBars.getBossBar
import io.github.toberocat.improvedfactions.translation.TranslatedBossBarsType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class RaidableBossBar {

    fun claimChanged(isRaidable: Boolean, player: Player, audience: Audience) {
        val raidableBossbar = player.getBossBar(TranslatedBossBarsType.RAIDABLE, ::createRaidableBar)
        when {
            isRaidable -> audience.showBossBar(raidableBossbar)
            else -> audience.hideBossBar(raidableBossbar)
        }
    }

    private fun createRaidableBar(name: Component) =
        BossBar.bossBar(name, 1f, BossBar.Color.RED, BossBar.Overlay.PROGRESS)
}