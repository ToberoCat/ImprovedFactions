package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.factions.FactionHandler
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Option
import org.bukkit.command.CommandSender

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
class FactionExistOption(
    private val index: Int, private val shouldExist: Boolean
) : Option {


    @Throws(CommandException::class)
    override fun execute(sender: CommandSender, args: Array<out String>): Array<out String> {
        if (index >= args.size) return args

        val arg = args[index]
        if (arg.isBlank()) return args

        var amount: Long = 0
        loggedTransaction {
            amount = FactionHandler.searchFactions(arg).count()
        }

        if (shouldExist && amount == 0L) throw CommandException("base.exceptions.faction-doesnt-exist", emptyMap())
        if (!shouldExist && amount > 0) throw CommandException("base.exceptions.faction-shouldnt-exist", emptyMap())
        return args
    }
}