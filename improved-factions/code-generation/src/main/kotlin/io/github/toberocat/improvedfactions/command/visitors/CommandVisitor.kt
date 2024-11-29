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
import io.github.toberocat.improvedfactions.command.data.CommandData
import io.github.toberocat.improvedfactions.command.data.CommandProcessFunction
import io.github.toberocat.improvedfactions.command.data.CommandProcessFunctionParameter
import io.github.toberocat.improvedfactions.command.generator.CommandCodeGenerator
import io.github.toberocat.improvedfactions.utils.getAnnotation
import io.github.toberocat.improvedfactions.utils.hasAnnotation

const val COMMAND_PROCESS_PREFIX = "process"

class CommandVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : KSVisitorVoid() {

    val commandProcessors = mutableListOf<String>()

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val commandData = extractCommandData(classDeclaration) ?: return
        generateCommandProcessor(commandData)
        commandProcessors.add("${commandData.targetPackage}.${commandData.targetName}Processor")
    }

    private fun extractCommandData(classDeclaration: KSClassDeclaration): CommandData? {
        val metaAnnotation = classDeclaration.getAnnotation<GeneratedCommandMeta>() ?: run {
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
        addDefaultResponses(responses, needsConfirmation)

        val commandData = CommandData(
            targetPackage = classDeclaration.packageName.asString(),
            targetName = classDeclaration.simpleName.asString(),
            label = metaAnnotation.label,
            module = metaAnnotation.module,
            responses = responses,
            processFunctions = processFunctions.mapNotNull { it.toCommandProcessFunction() }.toList(),
            needsConfirmation = needsConfirmation,
            permissionConfig = classDeclaration.getAnnotation<PermissionConfig>()?.config ?: PermissionConfigurations.ALL
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
            .filter { it.simpleName.asString().startsWith("process") }

    private fun KSFunctionDeclaration.toCommandProcessFunction(): CommandProcessFunction? {
        val parameters = parameters.mapIndexed { index, param ->
            val type = param.type.resolve().declaration.qualifiedName?.asString() ?: return null
            val isManual = param.hasAnnotation<ManualArgument>()
            CommandProcessFunctionParameter(
                simpleName = param.name?.asString() ?: "arg$index",
                variableName = param.name?.asString() ?: "arg$index",
                type = type,
                isRequired = !param.type.resolve().isMarkedNullable,
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