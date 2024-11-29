package io.github.toberocat.improvedfactions.command.generator

import io.github.toberocat.improvedfactions.annotations.CommandResponse
import io.github.toberocat.improvedfactions.command.data.CommandData
import io.github.toberocat.improvedfactions.command.data.CommandProcessFunctionParameter
import io.github.toberocat.improvedfactions.utils.camlCaseToSnakeCase
import java.io.BufferedWriter

class CommandContextCodeGenerator(
    private val writer: BufferedWriter,
    private val commandData: CommandData,
) {
    fun generateCommandContextClass() {
        writer.write(
            """
            
            abstract class ${commandData.targetName}Context(val parsers: Map<Class<*>, ArgumentParser>) : CommandProcessor {
                
                constructor() : this(DEFAULT_PARSERS)
                
                fun <T> getArgumentParser(sender: CommandSender, clazz: Class<T>, arg: String): T {
                    return getArgumentParser(clazz)?.parse(sender, arg) as? T ?: throw IllegalArgumentException("Unknown argument parser for ${"\$clazz"}")
                }
                
                fun getArgumentParser(clazz: Class<*>) = parsers[clazz]
                
                
                @Permission("${commandData.permission}", byDefault = ${commandData.permissionsByDefault})
                override fun canExecute(sender: CommandSender, args: Array<String>): Boolean {
                    val supportedSender = when (sender) {
                        ${generateSupportedSenders()}
                        else -> false
                    }
                    if (!supportedSender) return false
                    
                    return sender.hasPermission("${commandData.permission}")
                }
 
        """.trimIndent()
        )

        generateAbstractMethods()

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
            @Localization("$key")
            protected fun ${response.responseName}(vararg args: Pair<String, String>): CommandProcessResult { 
                return CommandProcessResult("$key", mapOf(*args))
            }
            
        """.trimIndent()
    }

    private fun generateAbstractMethods() {
        commandData.processFunctions
            .flatMap { function ->
                function.parameters
                    .filter { it.isManual }
                    .map { it.variableName }
            }
            .distinct()
            .forEach { variableName ->
                writer.write("abstract fun ${variableName}Argument(): ArgumentParser\n")
            }
    }

    private fun generateSupportedSenders(): String {
        return commandData.processFunctions
            .map { it.senderClass }
            .distinct()
            .joinToString("\n") {
                """
                    is $it -> return true
                """.trimIndent()
            }
    }
}