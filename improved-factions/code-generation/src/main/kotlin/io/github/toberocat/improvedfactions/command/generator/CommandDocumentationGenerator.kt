package io.github.toberocat.improvedfactions.command.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.commands.data.CommandData
import io.github.toberocat.improvedfactions.commands.data.CommandProcessFunction
import io.github.toberocat.improvedfactions.commands.data.CommandProcessFunctionParameter
import io.github.toberocat.improvedfactions.utils.localization

class CommandDocumentationGenerator(
    private val codeGenerator: CodeGenerator,
    private val commands: List<CommandData>,
) {
    fun generateDocumentation() {
        commands.forEach { command ->
            generateCommandDocumentation(command)
        }
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
            // Front Matter
            writer.write("---\n")
            writer.write("id: ${command.label}\n")
            writer.write("title: ${command.label.capitalize()}\n")
            writer.write("---\n\n")

            // Main Heading
            writer.write("# ${command.label.capitalize()}\n\n")

            // Note on Localization Placeholders
            writer.write("> **Note:** Localization keys are placeholders and should be replaced with actual values from your localization files.\n\n")

            // Description Section
            writer.write("## Description\n\n")
            writer.write("`")
            writer.write(getCommandDescription(command))
            writer.write("`\n\n")

            // Usage Section
            writer.write("## Usage\n\n")
            command.processFunctions.forEach { function ->
                writer.write("### For ${function.simpleSenderName()} ${getSenderIcon(function.simpleSenderName())}\n\n")
                writer.write("```bash\n")
                writer.write(getUsage(command, function))
                writer.write("\n```\n\n")
            }

            // Parameters Section
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

            // Permissions Section
            writer.write("## Permissions\n\n")
            writer.write("🔒 **Permission Required:** `${command.permission}`\n\n")

            // Responses Section
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

            // Closing Notes
            writer.write("---\n")
            writer.write("**Remember**: Replace localization keys with actual text from your localization files for meaningful descriptions.\n")
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
            if (param.isRequired) {
                sb.append("<${param.simpleName}>")
            } else {
                sb.append("[${param.simpleName}]")
            }
        }
        if (command.needsConfirmation) {
            sb.append(" [confirm]")
        }
        return sb.toString()
    }

    private fun getParameterDescription(commandData: CommandData, param: CommandProcessFunctionParameter): String {
        return param.getDescription(commandData)
    }

    private fun getResponseDescription(commandData: CommandData, response: CommandResponse): String {
        return response.localization(commandData)
    }

    private fun getSenderIcon(senderName: String): String {
        return when (senderName) {
            "Player" -> "👤"
            "Console" -> "🖥️"
            else -> ""
        }
    }
}