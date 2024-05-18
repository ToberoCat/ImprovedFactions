package io.github.toberocat.improvedfactions.components.rank

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.improvedfactions.ranks.FactionRanks

class FactionRankComponentBuilder : AbstractGuiComponentBuilder<FactionRankComponentBuilder>() {

    private var rank: String = ""

    fun setRank(rank: FactionRank): FactionRankComponentBuilder {
        this.rank = rank.name
        return this
    }

    override fun createComponent(): FactionRankComponent = loggedTransaction {
        val rank = FactionRank.find { FactionRanks.name eq rank }.firstOrNull()
            ?: throw InvalidGuiComponentException("Rank hasn't been found in the database")
        return@loggedTransaction FactionRankComponent(
            x,
            y,
            priority,
            id,
            clickFunctions,
            dragFunctions,
            closeFunctions,
            rank,
            hidden
        )
    }

    override fun deserialize(node: ParserContext) {
        super.deserialize(node)
        rank = node.string("rank").require(this)
    }
}