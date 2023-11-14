package io.github.toberocat.improvedfactions.utils.arguments

import io.github.toberocat.improvedfactions.factions.Faction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

class OptionalFactionArgument : Argument<Faction> {
    override fun parse(player: Player, arg: String): Faction =
        transaction { FactionHandler.searchFactions(arg).firstOrNull() } ?: throw CommandException(
            "base.exceptions.faction-not-found", emptyMap()
        )

    override fun defaultValue(player: Player): Faction =
        transaction { player.factionUser().faction() } ?: throw CommandException(
            "base.exceptions.faction-not-found", emptyMap()
        )

    override fun tab(sender: Player): List<String> =
        transaction { return@transaction Faction.all().map { x -> x.name } }

    override fun descriptionKey(): String = "base.command.args.optional-faction"

    override fun usage(): String = "[faction]"
}