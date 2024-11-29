package io.github.toberocat.improvedfactions.command.generator

import io.github.toberocat.improvedfactions.command.data.CommandData
import io.github.toberocat.improvedfactions.command.data.CommandProcessFunction
import java.io.OutputStream


class CommandCodeGenerator(private val commandData: CommandData) {

    fun generateCode(outputStream: OutputStream) {
        outputStream.bufferedWriter().use { writer ->
            writer.write(generateHeader())
            writer.write(generateExecuteMethod())
            writer.write(generateTabCompleteMethod())
            writer.write(generateProcessFunctions())
            writer.write(generateHandleTabCompletionMethod())
            writer.write("}\n")
            CommandContextCodeGenerator(writer, commandData).generateCommandContextClass()
        }
    }

    private fun generateHeader(): String {
        return """
        package ${commandData.targetPackage}

        import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
        import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
        import io.github.toberocat.improvedfactions.commands.executor.CommandExecutor
        import io.github.toberocat.improvedfactions.commands.CommandProcessResult
        import io.github.toberocat.improvedfactions.commands.CommandProcessor
        import io.github.toberocat.improvedfactions.annotations.localization.Localization
        import io.github.toberocat.improvedfactions.annotations.permission.Permission
        import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
        import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
        import io.github.toberocat.improvedfactions.commands.executor.DEFAULT_PARSERS
        import org.bukkit.command.CommandSender

        open class ${commandData.targetName}Processor(
            protected val plugin: ImprovedFactionsPlugin,
            private val executor: CommandExecutor
        ) : ${commandData.targetName}(), CommandProcessor {
            override val label = "${commandData.label}"

        """.trimIndent()
    }

    private fun generateExecuteMethod(): String {
        val cases = commandData.processFunctions.joinToString("\n") { function ->
            "sender is ${function.senderClass} && args.size == ${function.totalArgumentCount(commandData.needsConfirmation)} -> ${function.functionName}Call(sender, args)"
        }

        val confirmationCheck = if (commandData.needsConfirmation) {
            """
            if (!confirmAction(args)) {
                return confirmationNeeded("command" to label)
            }
            """.trimIndent()
        } else {
            ""
        }

        return """
            override fun execute(sender: CommandSender, args: Array<String>): CommandProcessResult {
                $confirmationCheck
                return when {
                    $cases
                    else -> missingRequiredArgument()
                }
            }

        """.trimIndent()
    }

    private fun generateTabCompleteMethod(): String {
        val cases = commandData.processFunctions.joinToString("\n") { function ->
            "is ${function.senderClass} -> handleTabCompletion(sender, args, ${function.parameters.size})"
        }

        return """
            override fun tabComplete(sender: CommandSender, args: Array<String>): List<String> {
                return when (sender) {
                    $cases
                    else -> emptyList()
                }
            }

        """.trimIndent()
    }

    private fun generateProcessFunctions(): String {
        return commandData.processFunctions.joinToString("\n") { function ->
            generateProcessFunction(function)
        }
    }

    private fun generateProcessFunction(function: CommandProcessFunction): String {
        val parameterExtraction = function.parameters.joinToString("\n") { parameter ->
            "val ${parameter.uniqueName} = parseArgument(sender, ${parameter.type}::class.java, args[${parameter.index}])"
        }

        val parameterNames = function.parameters.joinToString(", ") { it.uniqueName }

        return """
            private fun ${function.functionName}Call(sender: ${function.senderClass}, args: Array<String>): CommandProcessResult {
                $parameterExtraction
                return loggedTransaction { ${function.functionName}(sender, $parameterNames) }
            }

        """.trimIndent()
    }

    private fun generateHandleTabCompletionMethod(): String {
        val builder = StringBuilder()

        val confirmationTabComplete = if (commandData.needsConfirmation) {
            """
            if (currentIndex == parameterCount) return listOf("confirm")
            """.trimIndent()
        } else {
            ""
        }

        builder.appendLine("""
            private fun handleTabCompletion(sender: CommandSender, args: Array<String>, parameterCount: Int): List<String> {
                val currentIndex = args.size - 1
                $confirmationTabComplete
                if (currentIndex >= parameterCount) return emptyList()
        
                return when (currentIndex) {
        """.trimIndent())

        val argumentTypesByIndex = mutableMapOf<Int, String>()

        commandData.processFunctions.forEach { function ->
            function.parameters.forEach { parameter ->
                if (!argumentTypesByIndex.containsKey(parameter.index)) {
                    argumentTypesByIndex[parameter.index] = parameter.type
                }
            }
        }

        argumentTypesByIndex.forEach { (index, type) ->
            builder.appendLine("""
                    $index -> getArgumentParser(${type}::class.java)?.tabComplete(sender, currentIndex, args) ?: emptyList()
            """.trimIndent())
        }

        builder.appendLine("""
                    else -> emptyList()
                }
            }
        """.trimIndent())

        return builder.toString()
    }
}