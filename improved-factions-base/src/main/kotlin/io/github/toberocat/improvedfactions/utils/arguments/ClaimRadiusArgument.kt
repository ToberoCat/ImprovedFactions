package io.github.toberocat.improvedfactions.utils.arguments

import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.entity.Player

class ClaimRadiusArgument : IntegerArgument("[radius]", "base.command.args.claim-radius") {
    override fun parse(player: Player, value: String): Int {
        val number = super.parse(player, value)
        if (number < 0) {
            throw CommandException("base.exception.claim-radius.positive", emptyMap())
        }
        if (number > MAX_RADIUS) {
            throw CommandException("base.exception.claim-radius.max", mapOf(
                "max-radius" to MAX_RADIUS.toString()
            ))
        }
        return number
    }

    companion object {
        var MAX_RADIUS = 10
    }
}