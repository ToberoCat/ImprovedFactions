package io.github.toberocat.improvedfactions.commands.general

import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.commands.CommandProcessor
import io.github.toberocat.improvedfactions.commands.executor.CommandExecutor
import io.github.toberocat.improvedfactions.commands.sendCommandResult
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.translation.getUnformattedLocalized
import io.github.toberocat.improvedfactions.utils.getMeta
import org.bukkit.command.CommandSender

@GeneratedCommandMeta(
    label = "help",
    category = CommandCategory.GENERAL_CATEGORY,
    module = BaseModule.MODULE_NAME,
    responses = [
        CommandResponse("helpHeader"),
        CommandResponse("helpCategoryOverview"),
        CommandResponse("helpCommandDetails"),
        CommandResponse("helpCommandNotFound"),
        CommandResponse("helpSuccess")
    ]
)
abstract class HelpCommand : HelpCommandContext() {

    private val categoryIndex: MutableMap<String, MutableList<String>> = HashMap()
    lateinit var commands: Map<String, CommandProcessor>

    fun initialize(executor: CommandExecutor) {
        commands = executor.commandProcessors
        commands.forEach { (commandLabel, processor) ->
            val category = processor.commandData.category
            categoryIndex.computeIfAbsent(category) { ArrayList() }.add(commandLabel)
        }
    }


    fun process(sender: CommandSender, command: String?): CommandProcessResult {
        if (command == null) {
            printCategoryOverview(sender)
            return helpSuccess()
        }

        return when {
            command.startsWith("category:") -> {
                val category = command.removePrefix("category:")
                printCategoryDetails(sender, category)
                helpSuccess()
            }

            else -> {
                val processor = commands[command] ?: return helpCommandNotFound("command" to command)
                printCommandDetails(sender, processor)
                helpSuccess()
            }
        }
    }

    private fun printCategoryOverview(sender: CommandSender) {
        sender.sendCommandResult(helpHeader())
        categoryIndex.keys.forEach { category ->
            sender.sendCommandResult(
                helpCategoryOverview(
                    "category-name" to sender.getUnformattedLocalized(category),
                    "category-id" to category
                )
            )
        }
    }

    private fun printCommandDetails(sender: CommandSender, processor: CommandProcessor) {
        val commandData = processor.commandData
        val baseCommand = commandData.label
        val function = commandData.processFunctions.firstOrNull { it.senderClass == sender::class.java.simpleName } ?: return
        val args = function.parameters.joinToString(" ") {
            val usage = sender.getUnformattedLocalized(it.getUsage(commandData))
            val description = sender.getUnformattedLocalized(it.getDescription(commandData))
            "<hover:show_text:'${description}'>" +
                    "<${if (usage.startsWith("<")) "aqua" else "gold"}>${usage}</hover>"
        }
        val rawArgs =function.parameters.joinToString(" ") { sender.getUnformattedLocalized(it.getUsage(commandData)) }
        val usage = "/$baseCommand $args".trim()
        val cmd = "/$baseCommand $rawArgs".trim()

        sender.sendCommandResult(
            helpCommandDetails(
                "usage" to usage,
                "description" to sender.getUnformattedLocalized(commandData.descriptionKey),
                "cmd" to cmd
            )
        )
    }

    private fun printCategoryDetails(sender: CommandSender, category: String) {
        sender.sendCommandResult(helpHeader())
        categoryIndex[category]?.mapNotNull { commands[it] }?.forEach { printCommandDetails(sender, it) }
    }
}