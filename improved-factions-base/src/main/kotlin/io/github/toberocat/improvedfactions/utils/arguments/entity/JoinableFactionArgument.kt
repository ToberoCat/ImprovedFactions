package io.github.toberocat.improvedfactions.utils.arguments.entity

import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionJoinType
import io.github.toberocat.improvedfactions.factions.Factions
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.and

class JoinableFactionArgument : FactionArgument() {
    override fun collectEntities(player: Player): List<String> = Faction
        .find { Factions.factionJoinType eq FactionJoinType.OPEN }
        .map { it.name }

    override fun find(player: Player, value: String): Faction? =
        Faction.find { Factions.name eq value and (Factions.factionJoinType eq FactionJoinType.OPEN) }
            .firstOrNull()

}