package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionJoinType
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "join",
    category = CommandCategory.MEMBER_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("joinedFaction"),
        CommandResponse("factionNotFound"),
        CommandResponse("factionNotOpen"),
        CommandResponse("alreadyInFaction")
    ]
)
abstract class JoinCommand : JoinCommandContext() {

    fun process(player: Player, faction: Faction?): CommandProcessResult {
        if (faction == null) {
            return factionNotFound()
        }

        val factionUser = player.factionUser()
        if (factionUser.isInFaction()) {
            return alreadyInFaction()
        }

        if (faction.factionJoinType != FactionJoinType.OPEN) {
            return factionNotOpen()
        }

        faction.join(player.uniqueId, faction.defaultRank)
        return joinedFaction("factionName" to faction.name)
    }
}
