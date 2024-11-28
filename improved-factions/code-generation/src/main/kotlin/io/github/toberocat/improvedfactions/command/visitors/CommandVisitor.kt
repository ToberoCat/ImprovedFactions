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
import io.github.toberocat.improvedfactions.annotations.ManualArgument
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

        val className = classDeclaration.simpleName.asString()
        val commandData = CommandData(
            targetPackage = classDeclaration.packageName.asString(),
            targetName = classDeclaration.simpleName.asString(),
            label = args["label"]?.toString() ?: "NoLabel",
            module = args["module"]?.toString() ?: "NoModule",
            responses = parseResponses(className, (args["responses"] as? List<Any>) ?: emptyList()),
            processFunctions = parseProcessFunctions(className, processFunctions),
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

    private fun parseResponses(className: String, responses: List<Any>): List<CommandResponse> {
        return responses.map { response ->
            val responseArgs = extractAnnotationArguments(response as KSAnnotation)
            CommandResponse(
                key = responseArgs["key"] as String,
                responseName = responseArgs["responseName"] as String,
            )
        }.apply {
            if (!this.any { it.responseName == "missingRequiredArgument" }) {
                logger.error("Command $className is missing a missingRequiredArgument response.")
            }
        }
    }

    private fun parseProcessFunctions(className: String, functions: List<KSFunctionDeclaration>) =
        functions.mapNotNull { parseProcessFunction(it) }.apply {
            if (this.isEmpty()) {
                logger.error("Command $className is missing a process function.")
            }

            val senderClasses = this.map { it.senderClass }.distinct()
            senderClasses.forEach { senderClass ->
                if (this.count { senderClass == it.senderClass } > 1) {
                    throw IllegalArgumentException("Command $className has multiple process functions with the same sender class ${senderClasses}.")
                }
            }
        }

    private fun parseProcessFunction(function: KSFunctionDeclaration): CommandProcessFunction? {
        if (!function.simpleName.asString().contains(COMMAND_PROCESS_PREFIX)) {
            return null
        }

        val parameters = function.parameters.map {
            val ksDeclaration = it.type.resolve()
            val annotation = it.annotations.find { a -> a.shortName.getShortName() == ManualArgument::class.simpleName }
            CommandProcessFunctionParameter(
                simpleName = ksDeclaration.declaration.simpleName.asString(),
                type = ksDeclaration.declaration.qualifiedName?.asString() ?: "",
                isRequired = !ksDeclaration.isMarkedNullable,
                variableName = it.name?.asString() ?: "",
                isManual = annotation != null,
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