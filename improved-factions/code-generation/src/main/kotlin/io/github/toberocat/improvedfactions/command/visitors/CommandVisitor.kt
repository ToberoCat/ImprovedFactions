package io.github.toberocat.improvedfactions.command.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import io.github.toberocat.improvedfactions.annotations.command.CommandConfirmation
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.command.ManualArgument
import io.github.toberocat.improvedfactions.annotations.command.PermissionConfig
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.data.CommandData
import io.github.toberocat.improvedfactions.commands.data.CommandProcessFunction
import io.github.toberocat.improvedfactions.commands.data.CommandProcessFunctionParameter
import io.github.toberocat.improvedfactions.command.generator.CommandCodeGenerator
import io.github.toberocat.improvedfactions.utils.findAnnotation
import io.github.toberocat.improvedfactions.utils.hasAnnotation

const val COMMAND_PROCESS_PREFIX = "process"

class CommandVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : KSVisitorVoid() {

    val commandProcessors = mutableListOf<CommandData>()

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val commandData = extractCommandData(classDeclaration) ?: return
        generateCommandProcessor(commandData)
        commandProcessors.add(commandData)
    }

    private fun extractCommandData(classDeclaration: KSClassDeclaration): CommandData? {
        val metaAnnotation = classDeclaration.findAnnotation<GeneratedCommandMeta>() ?: run {
            logger.error("Class ${classDeclaration.simpleName.asString()} is missing @GeneratedCommandMeta annotation.")
            return null
        }

        val processFunctions = classDeclaration.getProcessFunctions().toList()
        if (processFunctions.isEmpty()) {
            logger.error("Class ${classDeclaration.simpleName.asString()} has no process functions starting with 'process'.")
            return null
        }

        val responses = metaAnnotation.responses.toMutableList()
        val needsConfirmation = classDeclaration.hasAnnotation<CommandConfirmation>()
        val hasNoArgsConstructor = classDeclaration.primaryConstructor?.parameters?.isEmpty() ?: false
        addDefaultResponses(responses, needsConfirmation)

        val commandData = CommandData(
            targetPackage = classDeclaration.packageName.asString(),
            targetName = classDeclaration.simpleName.asString(),
            label = metaAnnotation.label,
            module = metaAnnotation.module,
            category = metaAnnotation.category,
            responses = responses,
            processFunctions = processFunctions.mapNotNull { it.toCommandProcessFunction(needsConfirmation) }.toList(),
            needsConfirmation = needsConfirmation,
            permissionConfig = classDeclaration.findAnnotation<PermissionConfig>()?.config ?: PermissionConfigurations.ALL,
            addPluginAsParameter = !hasNoArgsConstructor
        )

        return commandData
    }

    private fun generateCommandProcessor(commandData: CommandData) {
        val outputStream = codeGenerator.createNewFile(
            Dependencies.ALL_FILES,
            commandData.targetPackage,
            "${commandData.targetName}Processor"
        )
        CommandCodeGenerator(commandData).generateCode(outputStream)
    }

    private fun addDefaultResponses(responses: MutableList<CommandResponse>, needsConfirmation: Boolean) {
        if (responses.none { it.responseName == "missingRequiredArgument" }) {
            responses.add(
                CommandResponse(
                    key = "",
                    responseName = "missingRequiredArgument",
                )
            )
        }

        if (needsConfirmation && responses.none { it.responseName == "confirmationNeeded" }) {
            responses.add(
                CommandResponse(
                    key = "",
                    responseName = "confirmationNeeded",
                )
            )
        }
    }

    private fun KSClassDeclaration.getProcessFunctions() =
        declarations.filterIsInstance<KSFunctionDeclaration>()
            .filter { it.simpleName.asString().startsWith(COMMAND_PROCESS_PREFIX) }

    private fun KSFunctionDeclaration.toCommandProcessFunction(needsConfirmation: Boolean): CommandProcessFunction? {
        val parameters = parameters.mapIndexed { index, param ->
            val type = param.type.resolve().declaration.qualifiedName?.asString() ?: return null
            val isManual = param.hasAnnotation<ManualArgument>()
            val isRequired = !param.type.resolve().isMarkedNullable
            if (needsConfirmation && !isRequired) {
                logger.error("Function ${simpleName.asString()} has optional parameters and requires confirmation. This is not supported.")
                return null
            }

            CommandProcessFunctionParameter(
                simpleName = param.name?.asString() ?: "arg$index",
                variableName = param.name?.asString() ?: "arg$index",
                type = type,
                isRequired = isRequired,
                isManual = isManual,
                index = index - 1  // Exclude sender parameter
            )
        }
        return CommandProcessFunction(
            senderClass = parameters.firstOrNull()?.type ?: return null,
            functionName = simpleName.asString(),
            parameters = parameters.drop(1)  // Exclude sender parameter
        )
    }
}