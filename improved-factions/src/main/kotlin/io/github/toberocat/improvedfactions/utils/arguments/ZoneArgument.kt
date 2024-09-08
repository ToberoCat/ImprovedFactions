package io.github.toberocat.improvedfactions.utils.arguments

import io.github.toberocat.improvedfactions.zone.Zone
import io.github.toberocat.improvedfactions.zone.ZoneHandler
import io.github.toberocat.toberocore.command.arguments.Argument
import org.bukkit.entity.Player

class ZoneArgument : Argument<Zone> {
    override fun parse(player: Player, value: String): Zone? = ZoneHandler.getZone(value)

    override fun tab(player: Player): List<String> = ZoneHandler.getZones().toList()

    override fun descriptionKey(): String = "base.command.args.zone"

    override fun usage(): String = "<zone>"
}