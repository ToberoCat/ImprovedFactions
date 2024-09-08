package io.github.toberocat.improvedfactions.utils.arguments.entity

import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.Factions
import org.bukkit.entity.Player

open class FactionArgument : EntityArgument<Faction>() {
    override fun collectEntities(player: Player): List<String> = Faction.all().map { it.name }

    override fun find(player: Player, value: String): Faction? = Faction.find { Factions.name eq value }.firstOrNull()

    override fun descriptionKey(): String = "base.command.args.faction"

    override fun usage(): String = "<faction>"
}