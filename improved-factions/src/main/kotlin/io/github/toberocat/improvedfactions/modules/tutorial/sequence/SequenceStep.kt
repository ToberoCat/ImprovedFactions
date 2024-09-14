package io.github.toberocat.improvedfactions.modules.tutorial.sequence

import org.bukkit.entity.Player

interface SequenceStep {
    fun execute(player: Player)
}