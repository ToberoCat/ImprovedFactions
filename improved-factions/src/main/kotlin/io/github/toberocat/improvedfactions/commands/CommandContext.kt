package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.translation.sendLocalized
import org.bukkit.command.CommandSender

interface CommandContext {
    val parsers: Map<Class<*>, ArgumentParser>

    fun getArgumentParser(clazz: Class<*>) = parsers[clazz]
}