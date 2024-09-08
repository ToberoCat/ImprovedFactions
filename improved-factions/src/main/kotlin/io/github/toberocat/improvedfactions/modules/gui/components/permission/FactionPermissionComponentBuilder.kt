package io.github.toberocat.improvedfactions.modules.gui.components.permission

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import io.github.toberocat.improvedfactions.permissions.FactionPermission
import io.github.toberocat.improvedfactions.permissions.FactionPermissions
import org.jetbrains.exposed.sql.and

class FactionPermissionComponentBuilder : AbstractGuiComponentBuilder<FactionPermissionComponentBuilder>() {
    var permission: String = ""
    var rank: Int = 1

    override fun createComponent(): FactionPermissionComponent {
        val permission = FactionPermission
            .find { FactionPermissions.permission eq permission and
                    (FactionPermissions.rankId eq rank) }.firstOrNull()
            ?: throw InvalidGuiComponentException("Rank hasn't been found in the database")
        return FactionPermissionComponent(
            permission,
            x,
            y,
            1,
            1,
            priority,
            id,
            clickFunctions,
            dragFunctions,
            closeFunctions,
            hidden
        )
    }

    override fun deserialize(node: ParserContext) {
        super.deserialize(node)
        permission = node.string("permission").require(this)
        rank = node.int("rank").require(this)
    }

    fun setPermission(permission: FactionPermission): FactionPermissionComponentBuilder {
        this.permission = permission.permission
        this.rank = permission.rankId
        return this
    }
}