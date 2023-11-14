package io.github.toberocat.improvedfactions.modules.power.handles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.configuration.file.FileConfiguration

interface FactionPowerRaidModuleHandle {
    fun memberJoin(faction: Faction)
    fun memberLeave(faction: Faction)
    fun claimChunk(faction: Faction)
    fun reloadConfig(plugin: ImprovedFactionsPlugin)
}