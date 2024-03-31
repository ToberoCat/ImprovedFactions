package io.github.toberocat.improvedfactions.utils.arguments

import io.github.toberocat.toberocore.command.arguments.Argument
import org.bukkit.entity.Player


open class EnumArgument<E : Enum<E>>(
    private val clazz: Class<E>,
    private val descriptionKey: String
) : Argument<E> {
    override fun parse(player: Player, arg: String): E = clazz.enumConstants.first { it.name.equals(arg, true) }

    override fun tab(player: Player) = clazz.enumConstants.map { it.name.lowercase() }

    override fun descriptionKey(): String = descriptionKey

    override fun usage(): String = "<${clazz.simpleName.lowercase()}>"
}