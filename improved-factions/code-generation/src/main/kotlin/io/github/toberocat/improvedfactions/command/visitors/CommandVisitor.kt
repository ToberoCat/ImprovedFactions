package io.github.toberocat.improvedfactions.command.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import io.github.toberocat.improvedfactions.annotations.CommandConfirmation
import io.github.toberocat.improvedfactions.annotations.CommandResponse
import io.github.toberocat.improvedfactions.annotations.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.annotations.ManualArgument
import io.github.toberocat.improvedfactions.command.data.CommandData
import io.github.toberocat.improvedfactions.command.data.CommandProcessFunction
import io.github.toberocat.improvedfactions.command.data.CommandProcessFunctionParameter
import io.github.toberocat.improvedfactions.command.generator.CommandCodeGenerator
import java.io.OutputStream
import kotlin.reflect.KClass

const val COMMAND_PROCESS_PREFIX = "process"

class CommandVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : KSVisitorVoid() {
    val commandProcessors = mutableListOf<String>()

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val args = findCommandAnnotations(classDeclaration)
        val processFunctions = findProcessFunctions(classDeclaration)

        val className = classDeclaration.simpleName.asString()
        val responses = parseResponses((args["responses"] as? List<Any>) ?: emptyList())
        val functions = parseProcessFunctions(className, processFunctions)
        val needsConfirmation = hasAnnotation(classDeclaration, CommandConfirmation::class)

        addMissingRequiredArgumentResponse(
            hasAnnotation(classDeclaration, CommandConfirmation::class),
            responses,
        )

        val commandData = CommandData(
            targetPackage = classDeclaration.packageName.asString(),
            targetName = classDeclaration.simpleName.asString(),
            label = args["label"]?.toString() ?: "NoLabel",
            module = args["module"]?.toString() ?: "NoModule",
            responses = responses,
            processFunctions = functions,
            needsConfirmation = needsConfirmation,
            permissionsByDefault = findAnnotation(classDeclaration, CommandConfirmation::class)?.let {
                it["permissionsByDefault"] as? Boolean ?: false
            } ?: false,
        )

        val outputStream = createFile(commandData)
        CommandCodeGenerator(commandData).generateCode(outputStream)

        commandProcessors.add("${commandData.targetPackage}.${commandData.targetName}Processor")
    }

    private fun createFile(commandData: CommandData): OutputStream {
        return codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = commandData.targetPackage,
            fileName = "${commandData.targetName}Processor",
        )
    }

    private fun addMissingRequiredArgumentResponse(
        needsConfirmation: Boolean,
        responses: MutableList<CommandResponse>,
    ) {
        if (!responses.any { it.responseName == "missingRequiredArgument" }) {
            responses.add(
                CommandResponse(
                    key = "",
                    responseName = "missingRequiredArgument",
                )
            )
        }

        if (needsConfirmation && !responses.any { it.responseName == "confirmationNeeded" }) {
            responses.add(
                CommandResponse(
                    key = "",
                    responseName = "confirmationNeeded",
                )
            )
        }
    }

    private fun parseResponses(responses: List<Any>): MutableList<CommandResponse> {
        return responses.map { response ->
            val responseArgs = extractAnnotationArguments(response as KSAnnotation)
            CommandResponse(
                key = responseArgs["key"] as String,
                responseName = responseArgs["responseName"] as String,
            )
        }.toMutableList()
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

    private fun hasAnnotation(annotation: KSClassDeclaration, name: KClass<*>) =
        annotation.annotations.any { it.shortName.getShortName() == name.simpleName }

    private fun findAnnotation(annotation: KSClassDeclaration, name: KClass<*>) =
        annotation.annotations.find { it.shortName.getShortName() == name.simpleName }?.let {
            extractAnnotationArguments(it)
        }

    private fun findCommandAnnotations(classDeclaration: KSClassDeclaration) =
        findAnnotation(classDeclaration, GeneratedCommandMeta::class)
            ?: throw IllegalArgumentException(
                "Class ${classDeclaration.simpleName.asString()} is missing " +
                        "${GeneratedCommandMeta::class.qualifiedName} annotation."
            )

    private fun findProcessFunctions(classDeclaration: KSClassDeclaration) =
        classDeclaration.declarations.filterIsInstance<KSFunctionDeclaration>().toList()
}