package io.github.toberocat.improvedfactions.command.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.toberocat.improvedfactions.annotations.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.command.visitors.CommandVisitor

class CommandProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(GeneratedCommandMeta::class.qualifiedName!!)

        val annotatedDeclarations = symbols.filterIsInstance<KSClassDeclaration>()

        val visitor = CommandVisitor(codeGenerator, logger)
        annotatedDeclarations.forEach { it.accept(visitor, Unit) }

        return emptyList()
    }
}