package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Option
import io.github.toberocat.toberocore.command.options.PlayerOption
import io.github.toberocat.toberocore.util.placeholder.PlaceholderBuilder
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

abstract class ArgumentOptions(private val index: Int) : Option {

    override fun execute(sender: CommandSender, args: Array<String>): Array<String> {
        if (index >= args.size) return args
        val valid = validate(args[index])
        if (!valid) {
            throw CommandException(
                argDoesntMatchKey(),
                PlaceholderBuilder().placeholder("position", index)
                    .placeholder("provided", args[index].length).placeholders
            )
        }
        return args
    }

    protected abstract fun validate(arg: String): Boolean
    protected abstract fun argDoesntMatchKey(): String
}

abstract class PlayerArgumentOptions(private val index: Int) : PlayerOption {

    override fun executePlayer(player: Player, args: Array<String>): Array<String> {
        if (index >= args.size) return args
        val valid = validate(player, args[index])
        if (!valid) {
            throw CommandException(
                argDoesntMatchKey(),
                PlaceholderBuilder().placeholder("position", index)
                    .placeholder("provided", args[index].length).placeholders
            )
        }
        return args
    }

    protected abstract fun validate(player: Player, arg: String): Boolean
    protected abstract fun argDoesntMatchKey(): String
}