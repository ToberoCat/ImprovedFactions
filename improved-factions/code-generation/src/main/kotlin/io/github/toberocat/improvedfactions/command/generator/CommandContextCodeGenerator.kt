package io.github.toberocat.improvedfactions.command.generator

import io.github.toberocat.improvedfactions.commands.data.CommandData
import io.github.toberocat.improvedfactions.utils.camlCaseToSnakeCase
import io.github.toberocat.improvedfactions.utils.localization
import java.io.BufferedWriter


class CommandContextCodeGenerator(
    private val writer: BufferedWriter,
    private val commandData: CommandData
) {
    fun generateCommandContextClass() {
        writer.write(generateClassHeader())
        writer.write(generateAbstractMethods())
        writer.write(generateResponseFunctions())
        writer.write("}\n")
    }

    private fun generateClassHeader(): String {
        return """
        abstract class ${commandData.targetName}Context(
            override val parsers: Map<Class<*>, ArgumentParser> = DEFAULT_PARSERS
        ) : CommandProcessor, CommandContext {
               
            @Permission("${commandData.permission}", config = PermissionConfigurations.${commandData.permissionConfig})
            override fun canExecute(sender: CommandSender, args: Array<String>): Boolean {
                return isSupportedSender(sender) && sender.hasPermission("${commandData.permission}")
            }

            private fun isSupportedSender(sender: CommandSender): Boolean {
                return when (sender) {
                    ${generateSupportedSenderCases()}
                    else -> false
                }
            }
            
            protected fun confirmAction(args: Array<String>): Boolean {
                return if (${commandData.needsConfirmation}) {
                    args.lastOrNull()?.equals("confirm", ignoreCase = true) ?: false
                } else {
                    true
                }
            }

        """.trimIndent()
    }

    private fun generateSupportedSenderCases(): String {
        return commandData.processFunctions
            .map { "is ${it.senderClass} -> true" }
            .distinct()
            .joinToString("\n")
    }

    private fun generateAbstractMethods(): String {
        val abstractMethods = commandData.processFunctions
            .asSequence()
            .flatMap { it.parameters }
            .filter { it.isManual }
            .map { "abstract fun ${it.variableName}Argument(): ArgumentParser" }
            .distinct()
            .joinToString("\n")

        return if (abstractMethods.isNotEmpty()) {
            "$abstractMethods\n"
        } else {
            ""
        }
    }

    private fun generateResponseFunctions(): String {
        return commandData.responses.joinToString("\n") { response ->
            val key = response.localization(commandData)
            """
            @Localization("$key")
            protected fun ${response.responseName}(vararg args: Pair<String, String>): CommandProcessResult {
                return CommandProcessResult("$key", mapOf(*args))
            }
            """.trimIndent()
        }
    }
}