package io.github.toberocat.improvedfactions.modules.pve.handle

import io.github.toberocat.improvedfactions.factions.Faction

interface PveModuleHandle {
    fun spawnRaid(faction: Faction)
}