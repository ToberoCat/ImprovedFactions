package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.toberocore.command.options.Option
import org.bukkit.command.CommandSender
import kotlin.math.min

class JoinedTextOption(private val index: Int, private val maxLiteralLength: Int = -1) : Option {

    override fun execute(sender: CommandSender, rawArgs: Array<String>): Array<String> {
        if (rawArgs.size <= index)
            return rawArgs

        val args = rawArgs.copyOfRange(index, rawArgs.size)
        val maxJoinLength = if (maxLiteralLength > 0) min(
            args.size,
            maxLiteralLength
        ) else args.size
        val builder = StringBuilder()

        for (i in 0 until maxJoinLength) {
            builder.append(args[i]).append(' ')
        }

        val array= arrayOf(*rawArgs.take(index).toTypedArray(), builder.toString().trim { it <= ' ' })
        return array
    }
}