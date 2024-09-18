package io.github.toberocat.improvedfactions.modules.tutorial.world

import org.bukkit.Location

data class TutorialAreaPoint(
    private val origin: Location,
    private val relativeX: Double,
    private val relativeY: Double,
    private val relativeZ: Double,
) {
    fun getLocation() = origin.clone().add(relativeX, relativeY, relativeZ)
}
