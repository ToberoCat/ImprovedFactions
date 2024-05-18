package io.github.toberocat.improvedfactions.modules.gui.components.icon

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler

class FactionIconComponentBuilder : AbstractGuiComponentBuilder<FactionIconComponentBuilder>() {
    var faction: Faction? = null

    override fun createComponent(): FactionIconComponent = FactionIconComponent(
        x,
        y,
        priority,
        id,
        clickFunctions,
        dragFunctions,
        closeFunctions,
        faction
            ?: throw InvalidGuiComponentException("The given faction name didn't return a faction"),
        hidden
    )

    override fun deserialize(node: ParserContext) {
        super.deserialize(node)
        val name = node.string("faction-name").require(this)
        faction = loggedTransaction { return@loggedTransaction FactionHandler.searchFactions(name).firstOrNull() }
    }
}