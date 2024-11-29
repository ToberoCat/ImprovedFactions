package io.github.toberocat.improvedfactions.command.generator

import io.github.toberocat.improvedfactions.command.data.CommandData
import io.github.toberocat.improvedfactions.command.data.CommandProcessFunction
import java.io.OutputStream

class CommandCodeGenerator(
    private val commandData: CommandData,
) {
    fun generateCode(outputStream: OutputStream) {
        outputStream.bufferedWriter().use { writer ->
            writer.write(
                """
                package ${commandData.targetPackage}
                
                import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
                import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser
                import io.github.toberocat.improvedfactions.commands.executor.CommandExecutor
                import io.github.toberocat.improvedfactions.commands.CommandProcessResult
                import io.github.toberocat.improvedfactions.annotations.Permission
                import io.github.toberocat.improvedfactions.commands.CommandProcessor
                import io.github.toberocat.improvedfactions.annotations.Localization
                import io.github.toberocat.improvedfactions.commands.executor.DEFAULT_PARSERS
                import org.bukkit.command.CommandSender
                
                open class ${commandData.targetName}Processor(
                    protected val plugin: ImprovedFactionsPlugin,
                    private val executor: CommandExecutor
                ) : ${commandData.targetName}(), CommandProcessor {
                    override val label = "${commandData.label}"
                    
                    override fun execute(sender: CommandSender, args: Array<String>): CommandProcessResult {
                         return when {
                         ${functionCallCases()}
                            else -> missingRequiredArgument()
                         }
                    }
                    
                    override fun tabComplete(sender: CommandSender, args: Array<String>): List<String> {
                        val currentIndex = 0.coerceAtLeast(args.size - 1)
                        val positionalArgumentParser: ArgumentParser? = when(sender) {
                            ${generateTabCompleteCases()}
                            else -> null
                        }
                        return positionalArgumentParser?.tabComplete(sender, currentIndex, args) ?: emptyList()
                    }
                    
            """.trimIndent()
            )

            commandData.processFunctions.forEach { function ->
                writer.write(generateCallProcessFunction(function))
            }

            writer.write("}")

            CommandContextCodeGenerator(writer, commandData).generateCommandContextClass()
        }
    }

    private fun getArgumentCount(function: CommandProcessFunction): Int {
        return when (commandData.needsConfirmation) {
            true -> function.parameters.size + 1
            false -> function.parameters.size
        }
    }

    private fun generateCallProcessFunction(function: CommandProcessFunction): String {
        return """
            
            private fun ${function.functionName}Call(sender: ${function.senderClass}, args: Array<String>): CommandProcessResult {
                ${checkIfConfirmation()}
                ${createParameterExtractors(function)}
                return ${function.functionName}(sender, ${function.parameters.joinToString { it.uniqueName }})
            }
            
        """.trimIndent()
    }

    private fun createParameterExtractors(function: CommandProcessFunction): String {
        return function.parameters.joinToString("\n") { parameter ->
            """
                val ${parameter.uniqueName} = getArgumentParser(sender, ${parameter.type}::class.java, args[${parameter.index}])
            """.trimIndent()
        }
    }

    private fun functionCallCases(): String {
        return commandData.processFunctions.joinToString("\n") { function ->
            """
                sender is ${function.senderClass} && args.size == ${getArgumentCount(function)} -> return ${function.functionName}Call(sender, args)
            """.trimIndent()
        }
    }

    private fun generateTabCompleteCases(): String {
        return commandData.processFunctions.joinToString("\n") { function ->
            """
                is ${function.senderClass} -> when (currentIndex) {
                    ${generateFunctionLevelTabCompleteCases(function)}
                    else -> null
                }
                
            """.trimIndent()
        }
    }

    private fun generateFunctionLevelTabCompleteCases(function: CommandProcessFunction): String {
        return function.parameters.joinToString("\n") { parameter ->
            """
                ${parameter.index} -> getArgumentParser(${parameter.type}::class.java)
            """.trimIndent()
        }
    }

    private fun checkIfConfirmation(): String {
        return when (commandData.needsConfirmation) {
            true -> """
                if (args.last() != "confirm") {
                    return confirmationNeeded("command" to label)
                }
            """.trimIndent()
            false -> ""
        }
    }
}