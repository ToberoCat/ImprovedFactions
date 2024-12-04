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
import io.github.toberocat.improvedfactions.utils.isSubtype
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
            val categoryLocale = processor.commandData.category
            val category = categoryLocale.substringAfterLast(".")
            categoryIndex.computeIfAbsent(category) { ArrayList() }.add(commandLabel)
        }
    }


    fun process(sender: CommandSender, command: String?): CommandProcessResult {
        if (command == null) {
            printCategoryOverview(sender)
            return helpSuccess()
        }

        val processor = commands[command]

        return when {
            processor == null || command.startsWith("c:") -> {
                val category = command.removePrefix("c:")
                if (!categoryIndex.containsKey(category)) {
                    return helpCommandNotFound()
                }

                printCategoryDetails(sender, category)
                helpSuccess()
            }

            else -> {
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
                    "category-id" to "c:$category"
                )
            )
        }
    }

    private fun printCommandDetails(sender: CommandSender, processor: CommandProcessor) {
        val commandData = processor.commandData
        val baseCommand = commandData.label
        val function =
            commandData.processFunctions.firstOrNull { sender::class.isSubtype(it.senderClass) } ?: return
        val args = function.parameters.joinToString(" ") {
            val usage = sender.getUnformattedLocalized(it.getUsage(commandData))
            val description = sender.getUnformattedLocalized(it.getDescription(commandData))
            "<hover:show_text:'${description}'>" +
                    "<${if (usage.startsWith("<")) "aqua" else "gold"}>${usage}</hover>"
        }
        val rawArgs = function.parameters.joinToString(" ") { sender.getUnformattedLocalized(it.getUsage(commandData)) }
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
        val commands = categoryIndex[category]?.mapNotNull { commands[it] } ?: return

        commands.forEach { printCommandDetails(sender, it) }
    }
}