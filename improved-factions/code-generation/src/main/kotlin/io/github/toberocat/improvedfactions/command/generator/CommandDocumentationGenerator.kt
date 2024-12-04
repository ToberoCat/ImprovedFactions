package io.github.toberocat.improvedfactions.command.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.commands.data.CommandData
import io.github.toberocat.improvedfactions.commands.data.CommandProcessFunction
import io.github.toberocat.improvedfactions.commands.data.CommandProcessFunctionParameter
import io.github.toberocat.improvedfactions.utils.localization
import java.io.File
import java.util.*

class CommandDocumentationGenerator(
    private val codeGenerator: CodeGenerator,
    private val commands: List<CommandData>,
    private val logger: KSPLogger,
    private val languageFolder: File
) {
    val properties = getPropertiesFile()

    fun generateDocumentation() {
        commands.forEach { command ->
            generateCommandDocumentation(command)
        }
    }

    private fun getPropertiesFile(): Properties {
        val propertiesFileName = "messages_en.properties"
        val properties = Properties()
        val file = File(languageFolder, propertiesFileName)
        if (!file.exists()) {
            logger.error("Localization file $propertiesFileName not found.")
            return properties
        }

        file.inputStream().use { properties.load(it) }
        return properties
    }

    private fun getLocalization(key: String): String {
        return properties.getProperty(key, key).replace("|", "\\|")
    }

    private fun generateCommandDocumentation(command: CommandData) {
        val fileName = command.label.replace(" ", "_")
        val outputStream = codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = "docs.commands.${command.module}",
            fileName = fileName,
            extensionName = "md"
        )
        outputStream.bufferedWriter().use { writer ->
            writer.write("---\n")
            writer.write("id: ${command.label}\n")
            writer.write("title: ${command.label.capitalize()}\n")
            writer.write("sidebar_label: ${command.label.capitalize()}\n")
            writer.write("---\n\n")

            writer.write("# ${command.label.capitalize()}\n\n")

            writer.write("## Description\n\n")
            writer.write(getLocalization(getCommandDescription(command)))
            writer.write("\n\n")

            writer.write("## Usage\n\n")
            command.processFunctions.forEach { function ->
                writer.write("### For ${function.simpleSenderName()} ${getSenderIcon(function.simpleSenderName())}\n\n")
                writer.write("```bash\n")
                writer.write(getUsage(command, function))
                writer.write("\n```\n\n")
            }

            writer.write("## Parameters\n\n")
            command.processFunctions.forEach { function ->
                if (function.parameters.isNotEmpty()) {
                    writer.write("### For ${function.simpleSenderName()} ${getSenderIcon(function.simpleSenderName())}\n\n")
                    writer.write("<details>\n")
                    writer.write("<summary>View Parameters</summary>\n\n")
                    writer.write("| Parameter | Type | Required | Description |\n")
                    writer.write("|-----------|------|----------|-------------|\n")
                    function.parameters.forEach { param ->
                        writer.write(
                            "| ${param.simpleName} | ${param.type.substringAfterLast('.')} | ${if (param.isRequired) "Yes" else "No"} | `${
                                getParameterDescription(
                                    command,
                                    param
                                )
                            }` |\n"
                        )
                    }
                    writer.write("\n")
                    writer.write("</details>\n\n")
                }
            }

            writer.write("## Permissions\n\n")
            writer.write("🔒 **Permission Required:** `${command.permission}`\n\n")

            writer.write("## Responses\n\n")
            if (command.responses.isNotEmpty()) {
                writer.write("| Response Code             | Description                                         |\n")
                writer.write("|---------------------------|-----------------------------------------------------|\n")
                command.responses.forEach { response ->
                    writer.write(
                        "| `${response.responseName}` | `${
                            getResponseDescription(
                                command,
                                response
                            )
                        }` |\n"
                    )
                }
                writer.write("\n")
            } else {
                writer.write("This command has no specific responses.\n\n")
            }
        }
    }

    private fun getCommandDescription(command: CommandData): String {
        return command.descriptionKey
    }

    private fun getUsage(command: CommandData, function: CommandProcessFunction): String {
        val sb = StringBuilder()
        sb.append("/").append(command.label)
        function.parameters.forEach { param ->
            sb.append(" ")
            val name = getLocalization(param.getUsage(command))
            sb.append("${name}")
        }
        if (command.needsConfirmation) {
            sb.append(" [confirm]")
        }
        return sb.toString()
    }

    private fun getParameterDescription(commandData: CommandData, param: CommandProcessFunctionParameter): String {
        return getLocalization(param.getDescription(commandData))
    }

    private fun getResponseDescription(commandData: CommandData, response: CommandResponse): String {
        return getLocalization(response.localization(commandData))
    }

    private fun getSenderIcon(senderName: String): String {
        return when (senderName) {
            "Player" -> "👤"
            "Console" -> "🖥️"
            else -> ""
        }
    }
}