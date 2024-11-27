package io.github.toberocat.improvedfactions.commands.executor

import io.github.toberocat.improvedfactions.commands.CommandProcessor
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.primitives.StringArgumentParser
import io.github.toberocat.improvedfactions.translation.sendLocalized
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabExecutor
import org.bukkit.plugin.java.JavaPlugin

val knownArgumentParsers = mapOf<Class<*>, ArgumentParser>(
    String::class.java to StringArgumentParser(),
)

class CommandExecutor(private val command: PluginCommand) : TabExecutor {
    companion object {
        fun create(plugin: JavaPlugin, label: String): CommandExecutor {
            val command = plugin.getCommand(label) ?: throw IllegalArgumentException("Command $label not found")
            return CommandExecutor(command)
        }
    }

    private val commandProcessors = mutableMapOf<String, CommandProcessor>()

    fun registerCommandProcessor(processor: CommandProcessor) {
        if (commandProcessors.containsKey(processor.label)) {
            throw IllegalArgumentException("Command processor with label ${processor.label} already registered")
        }

        commandProcessors[processor.label] = processor
        commandProcessors.entries.sortedByDescending { it.key.split(" ").size }
    }

    fun <T> getArgumentParser(clazz: Class<T>, arg: String): T =
        knownArgumentParsers[clazz]?.parse(arg) as? T ?: throw IllegalArgumentException("Unknown argument parser for $clazz")

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>,
    ): List<String> {
        TODO("Not yet implemented")
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>,
    ): Boolean {
        val processor = commandProcessors.entries.firstOrNull { it.key == label }?.value
        if (processor == null) {
            sender.sendMessage("Command $label not found")
            return false
        }

        val result = processor.execute(sender, args)
        if (result != null) {
            sender.sendLocalized(result.responseLocalizationKey, result.args)
        }

        return true
    }
}