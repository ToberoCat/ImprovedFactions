package io.github.toberocat.improvedfactions.modules.power.listener

import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener(private val powerRaidModuleHandleImpl: FactionPowerRaidModuleHandleImpl) : Listener {

    @EventHandler
    private fun onDeath(event: PlayerDeathEvent) {
        if (event.entity.world.name !in BaseModule.config.allowedWorlds)
            return

        val factionId = StorageManager.cache.user(event.entity.uniqueId)?.factionId ?: return
        val faction = StorageManager.cache.faction(factionId) ?: return
        powerRaidModuleHandleImpl.playerDie(faction)
    }
}
