package io.github.toberocat.improvedfactions.commands.executor

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.CommandProcessor
import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.primitives.BoolArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.primitives.IntArgumentParser
import io.github.toberocat.improvedfactions.commands.arguments.primitives.StringArgumentParser
import io.github.toberocat.improvedfactions.translation.sendLocalized
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

val DEFAULT_PARSERS = mapOf<Class<*>, ArgumentParser>(
    String::class.java to StringArgumentParser(),
    Int::class.java to IntArgumentParser(),
    Boolean::class.java to BoolArgumentParser(),
)

open class CommandExecutor(private val plugin: ImprovedFactionsPlugin) : TabExecutor {

    fun bindToPluginCommand(command: String) {
        val pluginCommand =
            plugin.getCommand(command) ?: throw IllegalArgumentException("Command $command not found")
        pluginCommand.tabCompleter = this
        pluginCommand.setExecutor(this)
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
        DEFAULT_PARSERS[clazz]?.parse(arg) as? T
            ?: throw IllegalArgumentException("Unknown argument parser for $clazz")

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        originalArgs: Array<String>,
    ): List<String> {
        val joinedCommand = createJoinedCommand(originalArgs)
        val processor = findProcessor(joinedCommand)
        if (processor == null) {
            sender.sendMessage("Command $joinedCommand not found")
            return emptyList()
        }

        val leftArgs = joinedCommand.replace(processor.label, "").trim()
        val argsArray = leftArgs.split(" ").filter { it.isNotBlank() }.toTypedArray()

        val rawResults = processor.tabComplete(sender, argsArray)
        val filteredResults = rawResults.filter { it.startsWith(argsArray.last(), ignoreCase = true) }
        val limitedResults = filteredResults.take(plugin.improvedFactionsConfig.maxCommandSuggestions)
        return limitedResults
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        originalArgs: Array<String>,
    ): Boolean {
        val joinedCommand = createJoinedCommand(originalArgs)
        val processor = findProcessor(joinedCommand)
        if (processor == null) {
            sender.sendMessage("Command $joinedCommand not found")
            return false
        }

        val leftArgs = joinedCommand.replace(processor.label, "").trim()
        val args = leftArgs.split(" ").filter { it.isNotBlank() }.toTypedArray()

        val result = processor.execute(sender, args)
        sender.sendLocalized(result.responseLocalizationKey, result.args)
        return true
    }

    private fun findProcessor(joinedCommand: String) =
        commandProcessors.entries.firstOrNull { joinedCommand.contains(it.key) }?.value

    private fun createJoinedCommand(args: Array<String>): String = args.joinToString(" ")
}