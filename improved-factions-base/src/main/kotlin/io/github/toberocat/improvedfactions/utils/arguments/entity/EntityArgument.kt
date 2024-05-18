package io.github.toberocat.improvedfactions.utils.arguments.entity

import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.entity.Player
import org.jetbrains.exposed.dao.IntEntity
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction 

abstract class EntityArgument<E : IntEntity> : Argument<E> {

    abstract fun collectEntities(player: Player): List<String>

    abstract fun find(player: Player, value: String): E?

    override fun parse(player: Player, value: String): E =
        loggedTransaction { find(player, value) } ?: throw CommandException(
            "base.command.args.no-valid-entity", emptyMap()
        )

    override fun tab(player: Player): List<String> =
        loggedTransaction { collectEntities(player) }
}