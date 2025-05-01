package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

class ClaimBlockInteractListener(zoneType: String) : ProtectionListener(
    zoneType,
    sendMessage = false
) {
    override fun namespace(): String = "block-interaction"

    @EventHandler
    fun interact(event: PlayerInteractEvent) {
        val mainHand = event.player.inventory.itemInMainHand
        val offHand = event.player.inventory.itemInOffHand

        if (event.clickedBlock?.type?.isInteractable == false && !isBucket(mainHand) && !isBucket(offHand))
            return
            
        protectChunk(event, event.clickedBlock, event.player)
    }

    fun isBucket(item: ItemStack?): Boolean {
        if (item == null) return false
        if (item.type == Material.BUCKET || item.type.name.endsWith("_BUCKET")) return true

        return false
    }
}