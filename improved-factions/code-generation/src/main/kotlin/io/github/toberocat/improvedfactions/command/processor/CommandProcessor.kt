package io.github.toberocat.improvedfactions.command.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.command.data.CommandData
import io.github.toberocat.improvedfactions.command.visitors.CommandVisitor
import io.github.toberocat.improvedfactions.utils.convertToCamelCase

class CommandProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>,
) : SymbolProcessor {

    private val collectedCommandProcessors = mutableListOf<CommandData>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(GeneratedCommandMeta::class.qualifiedName!!)
        val annotatedDeclarations = symbols.filterIsInstance<KSClassDeclaration>()

        val visitor = CommandVisitor(codeGenerator, logger)
        annotatedDeclarations.forEach { it.accept(visitor, Unit) }
        collectedCommandProcessors.addAll(visitor.commandProcessors)

        return emptyList()
    }

    override fun finish() {
        val file = codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = "io.github.toberocat.improvedfactions.commands.processor",
            fileName = "FactionCommandProcessors",
        )
        file.bufferedWriter().use { writer ->
            writer.write(generateProcessorList())
        }
    }

    private fun generateProcessorList(): String {
        val functions = getProcessorModules().map { (module, data) ->
            val processors = getProcessors(data)
            """
                
            fun ${module}CommandProcessors(plugin: ImprovedFactionsPlugin) = listOf<CommandProcessor>(
                ${processors.joinToString(",\n") { "$it(plugin)" }}
            )
            
            """.trimIndent()
        }.joinToString("\n")

        return """
        package io.github.toberocat.improvedfactions.commands.processor

        import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
        import io.github.toberocat.improvedfactions.commands.executor.CommandExecutor
        import io.github.toberocat.improvedfactions.commands.CommandProcessor

        $functions
        """.trimIndent()
    }

    private fun getProcessors(data: List<CommandData>) = data.map {
        "${it.targetPackage}.${it.targetName}Processor"
    }

    private fun getProcessorModules() =
        collectedCommandProcessors.groupBy { it.module }
            .mapKeys { it.key.convertToCamelCase() }
}