package io.github.toberocat.improvedfactions.processors.command

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import io.github.toberocat.improvedfactions.annotations.CommandMeta

class CommandVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : KSVisitorVoid() {
    val commandMetaDataList = mutableListOf<CommandMetaData>()

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val annotation = classDeclaration.annotations.find {
            it.shortName.getShortName() == CommandMeta::class.simpleName
        }

        if (annotation != null) {
            val args = annotation.arguments.associate { it.name?.asString() to it.value?.toString() }
            val qualifiedName = classDeclaration.simpleName.asString()
            val permission = args["permission"] ?: "NoPermission"
            val description = args["description"] ?: "NoDescription"
            val category = args["category"] ?: "Uncategorized"
            val module = args["module"] ?: "UnknownModule"

            commandMetaDataList.add(
                CommandMetaData(
                    qualifiedName = qualifiedName,
                    permission = permission,
                    description = description,
                    category = category,
                    module = module
                )
            )
        } else {
            logger.warn("Class ${classDeclaration.simpleName.asString()} is missing @CommandMeta annotation.")
        }
    }
}