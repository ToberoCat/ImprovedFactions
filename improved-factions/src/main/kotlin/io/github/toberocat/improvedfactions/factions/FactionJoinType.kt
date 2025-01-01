package io.github.toberocat.improvedfactions.factions

enum class FactionJoinType {
    OPEN,
    INVITE_ONLY;

    override fun toString() = name.lowercase().replace("_", " ")
}