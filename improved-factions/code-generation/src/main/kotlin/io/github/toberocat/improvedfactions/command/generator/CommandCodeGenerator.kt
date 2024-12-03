package io.github.toberocat.improvedfactions.command.generator

import io.github.toberocat.improvedfactions.commands.data.CommandData
import io.github.toberocat.improvedfactions.commands.data.CommandProcessFunction
import io.github.toberocat.improvedfactions.commands.data.CommandProcessFunctionParameter
import java.io.OutputStream


class CommandCodeGenerator(private val commandData: CommandData) {

    fun generateCode(outputStream: OutputStream) {
        outputStream.bufferedWriter().use { writer ->
            writer.write(generateHeader())
            writer.write(generateCommandDataGetter())
            writer.write(generateExecuteMethod())
            writer.write(generateTabCompleteMethod())
            writer.write(generateProcessFunctions())
            writer.write(generateHandleTabCompletionMethod())
            writer.write("}\n")
            CommandContextCodeGenerator(writer, commandData).generateCommandContextClass()
        }
    }

    private fun generateHeader(): String {
        val constructorArgs = if (commandData.addPluginAsParameter) "plugin" else ""

        return """
        package ${commandData.targetPackage}

        import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
        import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
        import io.github.toberocat.improvedfactions.commands.executor.CommandExecutor
        import io.github.toberocat.improvedfactions.commands.CommandProcessResult
        import io.github.toberocat.improvedfactions.commands.CommandProcessor
        import io.github.toberocat.improvedfactions.commands.CommandContext
        import io.github.toberocat.improvedfactions.annotations.localization.Localization
        import io.github.toberocat.improvedfactions.annotations.permission.Permission
        import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
        import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
        import io.github.toberocat.improvedfactions.commands.executor.DEFAULT_PARSERS
        import io.github.toberocat.improvedfactions.commands.arguments.ParsingContext
        import io.github.toberocat.improvedfactions.commands.data.CommandData
        import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
        import io.github.toberocat.improvedfactions.commands.data.CommandProcessFunction
        import io.github.toberocat.improvedfactions.commands.data.CommandProcessFunctionParameter
        import org.bukkit.command.CommandSender

        @Localization("${commandData.descriptionKey}")
        open class ${commandData.targetName}Processor(
            protected val plugin: ImprovedFactionsPlugin
        ) : ${commandData.targetName}($constructorArgs), CommandProcessor {
            override val label = "${commandData.label}"

        """.trimIndent()
    }

    private fun generateExecuteMethod(): String {
        val cases = commandData.processFunctions.joinToString("\n") { function ->
            "sender is ${function.senderClass} -> ${function.functionName}Call(sender, args)"
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
                return loggedTransaction { 
                    when {
                        $cases
                        else -> missingRequiredArgument()
                    }
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
                return loggedTransaction { 
                    when (sender) {
                        $cases
                        else -> emptyList()
                    }
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
            val typeCast = when {
                parameter.isRequired -> "as ${parameter.type}"
                else -> "as? ${parameter.type}"
            }

            "val ${parameter.uniqueName} = ${getArgumentParserCode(parameter)}!!.parse(sender, ${parameter.index}, args) $typeCast"
        }

        val argumentSizeCheck = """
            if (args.size < ${function.getMinParametersCount(commandData.needsConfirmation)} || args.size > ${
            function.getMaxParametersCount(commandData.needsConfirmation)
        }) {
                return missingRequiredArgument()
            }
            """.trimIndent()


        val parameterNames = function.parameters.joinToString(", ") { it.uniqueName }

        return """
            private fun ${function.functionName}Call(sender: ${function.senderClass}, args: Array<String>): CommandProcessResult {
                $argumentSizeCheck
                $parameterExtraction
                return ${function.functionName}(sender, $parameterNames)
            }

        """.trimIndent()
    }

    private fun generateCommandDataGetter(): String {
        return """
                override val commandData = $commandData
                    
        """.trimIndent()
    }

    private fun generateHandleTabCompletionMethod(): String {
        val builder = StringBuilder()

        val argumentTypesByIndex = mutableMapOf<Int, CommandProcessFunctionParameter>()

        commandData.processFunctions.forEach { function ->
            function.parameters.forEach { parameter ->
                if (!argumentTypesByIndex.containsKey(parameter.index)) {
                    argumentTypesByIndex[parameter.index] = parameter
                }
            }
        }


        val confirmationTabComplete = if (commandData.needsConfirmation) {
            """
            if (currentIndex == parameterCount) return listOf("confirm")
            """.trimIndent()
        } else {
            ""
        }

        val localizationAnnotations = argumentTypesByIndex.values.joinToString("\n") { param ->
            """@Localization("${param.getUsage(commandData)}")
               @Localization("${param.getDescription(commandData)}")
            """.trimIndent()
        }

        builder.appendLine(
            """
            $localizationAnnotations
            private fun handleTabCompletion(sender: CommandSender, args: Array<String>, parameterCount: Int): List<String> {
                val currentIndex = args.size - 1
                $confirmationTabComplete
                if (currentIndex >= parameterCount) return emptyList()
        
                return when (currentIndex) {
        """.trimIndent()
        )

        argumentTypesByIndex.forEach { (index, param) ->
            val parsingContext =
                """ParsingContext(sender, args, currentIndex, "${param.getUsage(commandData)}")"""

            builder.appendLine(
                """
                    $index -> ${getArgumentParserCode(param)}!!.tabComplete(${parsingContext})
            """.trimIndent()
            )
        }

        builder.appendLine(
            """
                    else -> emptyList()
                }
            }
        """.trimIndent()
        )

        return builder.toString()
    }

    private fun getArgumentParserCode(parameter: CommandProcessFunctionParameter): String {
        if (parameter.isManual) {
            return "${parameter.variableName}Argument()"
        }

        return "getArgumentParser(${parameter.type}::class.java)"
    }
}