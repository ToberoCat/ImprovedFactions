package io.github.toberocat.improvedfactions.modules.pve.handle

import io.github.toberocat.improvedfactions.factions.Faction

class DummyPveModuleHandle : PveModuleHandle {
    override fun spawnRaid(faction: Faction) = Unit
}