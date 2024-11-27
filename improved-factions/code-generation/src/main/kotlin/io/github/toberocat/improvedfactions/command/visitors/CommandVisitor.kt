package io.github.toberocat.improvedfactions.command.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import io.github.toberocat.improvedfactions.annotations.CommandResponse
import io.github.toberocat.improvedfactions.annotations.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.command.data.CommandData
import io.github.toberocat.improvedfactions.command.data.CommandProcessFunction
import io.github.toberocat.improvedfactions.command.data.CommandProcessFunctionParameter
import io.github.toberocat.improvedfactions.command.generator.CommandCodeGenerator
import java.io.OutputStream

const val COMMAND_PROCESS_PREFIX = "process"

class CommandVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val commandAnnotations = findCommandAnnotations(classDeclaration)
        val processFunctions = findProcessFunctions(classDeclaration)
        val args = extractAnnotationArguments(commandAnnotations)

        val commandData = CommandData(
            targetPackage = classDeclaration.packageName.asString(),
            targetName = classDeclaration.simpleName.asString(),
            label = args["label"]?.toString() ?: "NoLabel",
            module = args["module"]?.toString() ?: "NoModule",
            responses = parseResponses((args["responses"] as? List<Any>) ?: emptyList()),
            processFunctions = parseProcessFunctions(classDeclaration.simpleName.asString(), processFunctions),
        )

        val outputStream = createFile(commandData)
        CommandCodeGenerator(commandData).generateCode(outputStream)
    }

    private fun createFile(commandData: CommandData): OutputStream {
        return codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = commandData.targetPackage,
            fileName = "${commandData.targetName}Processor",
        )
    }

    private fun parseResponses(responses: List<Any>): List<CommandResponse> {
        return responses.map { response ->
            val responseArgs = extractAnnotationArguments(response as KSAnnotation)
            CommandResponse(
                key = responseArgs["key"] as String,
                responseName = responseArgs["responseName"] as String,
            )
        }.apply {
            if (!this.any { it.responseName == "missingRequiredArgument" }) {
                throw IllegalArgumentException("Command is missing a missingRequiredArgument response.")
            }
        }
    }

    private fun parseProcessFunctions(className: String, functions: List<KSFunctionDeclaration>) =
        functions.mapNotNull { parseProcessFunction(it) }.apply {
            if (this.isEmpty()) {
                throw IllegalArgumentException("Command is missing a process function.")
            }

            // When the same sender class is used, the arguments must match. Only the length can differ.
            val senderClassGroups: Map<String, List<CommandProcessFunction>> = this.groupBy { it.senderClass }
            senderClassGroups.forEach { (_, functions) ->
                val firstFunction = functions.first()
                functions.forEach {
                    if (it.parameters.size != firstFunction.parameters.size) {
                        throw IllegalArgumentException("Command process functions must have the same amount of parameters. " +
                                "${it.functionName} in command $className has ${it.parameters.size} parameters, but ${firstFunction.functionName} has ${firstFunction.parameters.size} parameters.")
                    }
                }
            }
        }

    private fun parseProcessFunction(function: KSFunctionDeclaration): CommandProcessFunction? {
        if (!function.simpleName.asString().contains(COMMAND_PROCESS_PREFIX)) {
            return null
        }

        val parameters = function.parameters.map {
            val ksDeclaration = it.type.resolve()
            CommandProcessFunctionParameter(
                simpleName = ksDeclaration.declaration.simpleName.asString(),
                type = ksDeclaration.declaration.qualifiedName?.asString() ?: "",
                index = function.parameters.indexOf(it) - 1,
            )
        }

        return CommandProcessFunction(
            senderClass = parameters.first().type,
            functionName = function.simpleName.asString(),
            parameters = parameters.drop(1),
        )
    }

    private fun extractAnnotationArguments(annotation: KSAnnotation): Map<String?, Any?> {
        return annotation.arguments.associate { it.name?.asString() to it.value }
    }

    private fun findCommandAnnotations(classDeclaration: KSClassDeclaration): KSAnnotation {
        val annotation = classDeclaration.annotations.find {
            it.shortName.getShortName() == GeneratedCommandMeta::class.simpleName
        }

        if (annotation == null) {
            throw IllegalArgumentException("Class ${classDeclaration.simpleName.asString()} is missing ${GeneratedCommandMeta::class.qualifiedName} annotation.")
        }
        return annotation
    }

    private fun findProcessFunctions(classDeclaration: KSClassDeclaration) =
        classDeclaration.declarations.filterIsInstance<KSFunctionDeclaration>().toList()
}