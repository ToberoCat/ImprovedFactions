package io.github.toberocat.improvedfactions.placeholder.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.toberocat.improvedfactions.annotations.papi.PapiPlaceholder
import io.github.toberocat.improvedfactions.placeholder.generator.PlaceholderDocumentationGenerator
import io.github.toberocat.improvedfactions.placeholder.visitor.PlaceholderVisitor

class PlaceholderProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private val placeholders = mutableListOf<PapiPlaceholder>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(PapiPlaceholder::class.qualifiedName!!)
        if (!symbols.iterator().hasNext()) {
            return emptyList()
        }

        logger.info("Processing placeholder symbols...")
        val visitor = PlaceholderVisitor()
        symbols.forEach { it.accept(visitor, Unit) }
        placeholders.addAll(visitor.placeholders)

        return emptyList()
    }

    override fun finish() {
        PlaceholderDocumentationGenerator(codeGenerator, placeholders).createPlaceholdersMd()
    }
}