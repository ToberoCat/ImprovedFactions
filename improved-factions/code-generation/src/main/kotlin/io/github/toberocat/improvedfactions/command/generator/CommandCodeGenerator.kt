package io.github.toberocat.improvedfactions.command.generator

import io.github.toberocat.improvedfactions.annotations.CommandResponse
import io.github.toberocat.improvedfactions.command.data.CommandData
import io.github.toberocat.improvedfactions.command.data.CommandProcessFunction
import io.github.toberocat.improvedfactions.utils.camlCaseToSnakeCase
import java.io.BufferedWriter
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
                         val currentIndex = args.size
                        val positionalArgumentParser = when (currentIndex) {
                            
                        }
                    }
                    
            """.trimIndent()
            )

            commandData.processFunctions.forEach { function ->
                writer.write(generateCallProcessFunction(function))
            }

            writer.write("}")

            generateCommandContextClass(writer)
        }
    }

    private fun generateCommandContextClass(writer: BufferedWriter) {
        writer.write(
            """
            
            open class ${commandData.targetName}Context(val parsers: Map<Class<*>, ArgumentParser>) {
                
                constructor() : this(DEFAULT_PARSERS)
                
                fun <T> getArgumentParser(sender: CommandSender, clazz: Class<T>, arg: String): T {
                    return parsers[clazz]?.parse(sender, arg) as? T ?: throw IllegalArgumentException("Unknown argument parser for ${"\$clazz"}")
                }
                
        """.trimIndent()
        )

        commandData.responses.forEach { response ->
            writer.write(generateResponseFunction(response))
        }

        writer.write("}")
    }

    private fun generateResponseFunction(response: CommandResponse): String {
        val key = when (response.key.isBlank()) {
            false -> response.key
            true -> "${commandData.localizedCommandLabel}.${response.responseName.camlCaseToSnakeCase("-")}"
        }

        return """
            protected fun ${response.responseName}(vararg args: Pair<String, String>): CommandProcessResult { 
                return CommandProcessResult("$key", mapOf(*args))
            }
            
        """.trimIndent()
    }

    private fun generateCallProcessFunction(function: CommandProcessFunction): String {
        return """
            
            private fun ${function.functionName}Call(sender: ${function.senderClass}, args: Array<String>): CommandProcessResult {
                if (args.size != ${function.parameters.size}) {
                    return missingRequiredArgument()
                }
                
                ${createParameterExtractors(function)}
                return ${function.functionName}(sender, ${function.parameters.joinToString { it.simpleName }})
            }
            
        """.trimIndent()
    }

    private fun createParameterExtractors(function: CommandProcessFunction): String {
        return function.parameters.joinToString("\n") { parameter ->
            """
                val ${parameter.simpleName} = getArgumentParser(sender, ${parameter.type}::class.java, args[${parameter.index}])
            """.trimIndent()
        }
    }

    private fun functionCallCases(): String {
        return commandData.processFunctions.joinToString("\n") { function ->
            """
                sender is ${function.senderClass} && args.size == ${function.parameters.size} -> return ${function.functionName}Call(sender, args)
            """.trimIndent()
        }
    }

    private fun generateTabCompleteCases(): String {
        return commandData.processFunctions.joinToString("\n") { function ->
            """
                ${function.parameters} -> {
                    val ${function.parameters.joinToString { it.simpleName }} = ${function.parameters.joinToString { it.simpleName }}Parser.parse(sender, args[currentIndex])
                }
            """.trimIndent()
        }
    }
}