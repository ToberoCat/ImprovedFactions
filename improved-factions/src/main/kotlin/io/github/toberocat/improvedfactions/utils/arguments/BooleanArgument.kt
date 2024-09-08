package io.github.toberocat.improvedfactions.utils.arguments

import io.github.toberocat.toberocore.command.arguments.Argument
import org.bukkit.entity.Player

class BooleanArgument : Argument<Boolean> {
    override fun parse(player: Player, value: String): Boolean = value.toBoolean()

    override fun defaultValue(player: Player): Boolean = false

    override fun tab(player: Player): List<String> = listOf("true", "false")

    override fun descriptionKey(): String = "base.command.args.bool"

    override fun usage(): String = "<bool>"
}