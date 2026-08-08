package io.github.toberocat.improvedfactions.commands.member

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.cancelledCommandResult
import io.github.toberocat.improvedfactions.commands.respondAfter
import io.github.toberocat.improvedfactions.api.events.FactionJoinEvent
import io.github.toberocat.improvedfactions.database.storage.*
import io.github.toberocat.improvedfactions.factions.FactionJoinType
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.bukkit.entity.Player
import org.bukkit.Bukkit

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

    fun process(player: Player, faction: FactionSnapshot?): CommandProcessResult? {
        if (faction == null) {
            return factionNotFound()
        }

        val factionUser = player.cachedUser()
        if (factionUser.isInFaction()) {
            return alreadyInFaction()
        }

        if (faction.joinType != FactionJoinType.OPEN.toString()) {
            return factionNotOpen()
        }

        val event = FactionJoinEvent(faction, factionUser)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return cancelledCommandResult()

        return player.respondAfter(GameStateCommands.setUserFaction(player.uniqueId, faction.id, faction.defaultRankId)) {
            joinedFaction("factionName" to faction.name)
        }
    }
}
