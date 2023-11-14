package io.github.toberocat.improvedfactions.modules.power.handles

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.factions.Faction
import org.bukkit.configuration.file.FileConfiguration


class DummyFactionPowerRaidModuleHandle : FactionPowerRaidModuleHandle {
    override fun memberJoin(faction: Faction) = Unit
    override fun memberLeave(faction: Faction) = Unit
    override fun claimChunk(faction: Faction) = Unit
    override fun reloadConfig(plugin: ImprovedFactionsPlugin) = Unit
}