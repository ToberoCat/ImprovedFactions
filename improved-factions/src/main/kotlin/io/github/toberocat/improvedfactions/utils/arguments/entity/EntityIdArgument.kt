package io.github.toberocat.improvedfactions.utils.arguments.entity

import org.bukkit.entity.Player
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder

abstract class EntityIdArgument<E : IntEntity>(private val entityClass: IntEntityClass<E>) : EntityArgument<E>() {
    abstract fun collector(player: Player): SqlExpressionBuilder.() -> Op<Boolean>

    override fun collectEntities(player: Player): List<String> =
        entityClass.find(collector(player)).map { it.id.value.toString() }

    override fun find(player: Player, value: String): E? = entityClass.findById(value.toIntOrNull() ?: -1)

    override fun usage(): String = "<id>"
}