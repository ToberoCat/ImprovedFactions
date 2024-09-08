package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.PlayerOption
import io.github.toberocat.toberocore.util.placeholder.PlaceholderBuilder
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class InFactionOption(private val inFaction: Boolean) : PlayerOption {
    @Throws(CommandException::class)
    override fun executePlayer(sender: Player, args: Array<String>): Array<String> {
        if (sender.factionUser().isInFaction() != inFaction) {
            throw CommandException(
                "base.exceptions.${if (inFaction) "need-faction" else "cant-be-in-faction"}",
                PlaceholderBuilder().placeholder("player", sender).placeholders
            )
        }
        return args
    }

    override fun show(sender: CommandSender, args: Array<out String>): Boolean {
        return (sender as? Player)?.factionUser()?.isInFaction() == inFaction
    }
}
