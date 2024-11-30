package io.github.toberocat.improvedfactions.modules.power.listener

import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.power.impl.FactionPowerRaidModuleHandleImpl
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener(private val powerRaidModuleHandleImpl: FactionPowerRaidModuleHandleImpl) : Listener {

    @EventHandler
    private fun onDeath(event: PlayerDeathEvent) {
        if (event.entity.world.name !in BaseModule.config.allowedWorlds)
            return

        loggedTransaction {
            val faction = event.entity.factionUser().faction() ?: return@loggedTransaction
            powerRaidModuleHandleImpl.playerDie(faction)
        }
    }
}