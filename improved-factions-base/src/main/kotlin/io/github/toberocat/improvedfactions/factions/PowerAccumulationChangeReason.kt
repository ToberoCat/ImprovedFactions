package io.github.toberocat.improvedfactions.factions

enum class PowerAccumulationChangeReason {
    PASSIV_ENERGY_ACCUMULATION,
    CHUNK_CLAIMED,
    CHUNK_KEEP_COST,
    MAX_CHANGED,
    PLAYER_DEATH;

    override fun toString() = name.lowercase().replace("_", "-")
}