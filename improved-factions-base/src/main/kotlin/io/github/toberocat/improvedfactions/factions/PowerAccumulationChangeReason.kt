package io.github.toberocat.improvedfactions.factions

enum class PowerAccumulationChangeReason {
    PASSIV_ENERGY_ACCUMULATION,
    CHUNK_CLAIMED,
    MAX_CHANGED;

    override fun toString() = name.lowercase().replace("_", "-")
}