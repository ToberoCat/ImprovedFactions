package io.github.toberocat.improvedfactions.claims.overclaim

import org.bukkit.scheduler.BukkitRunnable

class SiegeTask(private val manager: ClaimSiegeManager) : BukkitRunnable() {
    override fun run() = manager.tickSiege()

    override fun cancel() {
        super.cancel()
        manager.resetState()
    }
}