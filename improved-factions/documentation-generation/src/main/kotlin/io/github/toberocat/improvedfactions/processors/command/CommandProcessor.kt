package io.github.toberocat.improvedfactions.processors.command

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.toberocat.improvedfactions.annotations.CommandMeta
import java.io.OutputStream

class CommandProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>,
) : SymbolProcessor {
    private val results = mutableMapOf<String, MutableList<CommandMetaData>>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(CommandMeta::class.qualifiedName!!)

        val annotatedDeclarations = symbols.filterIsInstance<KSClassDeclaration>()

        val visitor = CommandVisitor(codeGenerator, logger)
        annotatedDeclarations.forEach { it.accept(visitor, Unit) }

        visitor.commandMetaDataList.forEach { meta ->
            val categoryList = results.getOrPut(meta.category) { mutableListOf() }
            categoryList.add(meta)
        }

        return emptyList()
    }

    override fun finish() {
        generateWiki(results)
    }

    private fun generateWiki(commandsByCategory: Map<String, MutableList<CommandMetaData>>) {
        commandsByCategory.forEach { (category, commands) ->
            val fileName = "commands_${category.replace(" ", "_").lowercase()}.md"
            val file: OutputStream = codeGenerator.createNewFile(
                dependencies = Dependencies.ALL_FILES,
                packageName = "wiki.commands",
                fileName = fileName,
                extensionName = "md"
            )

            file.bufferedWriter().use { writer ->
                writer.write("# $category Commands\n\n")
                commands.forEach { command ->
                    writer.write("## ${command.qualifiedName}\n\n")
                    writer.write("**Permission**: `${command.permission}`\n\n")
                    writer.write("**Description**: ${command.description}\n\n")
                    writer.write("**Module**: ${command.module}\n\n")
                    // Add more details as needed
                    writer.write("---\n\n")
                }
            }

            logger.info("Generated wiki file: $fileName")
        }

        // Generate index file
        val indexFile: OutputStream = codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = "wiki.commands",
            fileName = "README",
            extensionName = "md"
        )

        indexFile.bufferedWriter().use { writer ->
            writer.write("# Command Index\n\n")
            commandsByCategory.keys.sorted().forEach { category ->
                val fileName = "commands_${category.replace(" ", "_").lowercase()}.md"
                writer.write("- [$category](commands/$fileName)\n")
            }
        }

        logger.info("Generated command index README.md")
    }
}