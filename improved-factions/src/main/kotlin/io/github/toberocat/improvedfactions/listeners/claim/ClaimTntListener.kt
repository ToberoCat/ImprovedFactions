package io.github.toberocat.improvedfactions.listeners.claim

import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.database.storage.claimKey
import io.github.toberocat.improvedfactions.user.noFactionId
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityExplodeEvent


class ClaimTntListener(zoneType: String) : ProtectionListener(zoneType) {


    override fun namespace(): String = "outside-explosions"


    @EventHandler
    fun onTntExplode(event: EntityExplodeEvent) {
        val cache = StorageManager.cache
        if (!cache.isReady()) {
            event.blockList().clear()
            return
        }
        val sourceFaction = cache.claim(event.entity.location.claimKey())?.factionId ?: noFactionId

        val iterator = event.blockList().iterator()
        while (iterator.hasNext()) {
            val block = iterator.next()
            val claim = cache.claim(block.claimKey()) ?: continue
            if (claim.zoneType != zoneType) continue
            if (claim.factionId != sourceFaction) iterator.remove()
        }
    }
}
