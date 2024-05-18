package io.github.toberocat.improvedfactions.listeners.claim

import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.vehicle.VehicleDamageEvent

class ClaimVehicleBreakListener(zoneType: String) : ProtectionListener(zoneType) {
    override fun namespace(): String = "minecart-damage"

    @EventHandler
    fun vehicleBreak(event: VehicleDamageEvent) {
        if (event.vehicle !is Minecart)
            return
        protectChunk(event, event.vehicle, event.attacker as? Player ?: return)
    }
}