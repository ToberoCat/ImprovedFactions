package io.github.toberocat.improvedfactions.modules.gui.components.rankselector

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

class FactionRankSelectorComponentBuilder : AbstractGuiComponentBuilder<FactionRankSelectorComponentBuilder>() {
    var player: String = ""

    override fun createComponent(): FactionRankSelectorComponent {
        val player = Bukkit.getOfflinePlayer(player)
        if (!player.hasPlayedBefore())
            throw InvalidGuiComponentException("Specified player ${this.player} has never played on the server before")
        return FactionRankSelectorComponent(
            player,
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
        player = node.string("player").require(this)
    }

    fun setPlayer(player: OfflinePlayer): FactionRankSelectorComponentBuilder {
        this.player = player.name ?: ""
        return this
    }
}