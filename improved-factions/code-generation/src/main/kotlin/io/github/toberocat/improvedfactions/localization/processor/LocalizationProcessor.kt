package io.github.toberocat.improvedfactions.localization.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.toberocat.improvedfactions.annotations.Localization
import io.github.toberocat.improvedfactions.localization.visitor.LocalizationVisitor
import kotlin.math.log

class LocalizationProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>,
) : SymbolProcessor {

    private val localizations = mutableListOf<String>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Localization::class.qualifiedName!!)
        logger.warn("Symbols: ${symbols.map { it::class.java }.toList()}")
        val visitor = LocalizationVisitor(logger, resolver)
        symbols.forEach { it.accept(visitor, Unit) }
        localizations.addAll(visitor.localizations)

        return emptyList()
    }

    override fun finish() {
        writePropertiesFile()
    }

    private fun writePropertiesFile() {

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = "", // Specify package name if needed
            fileName = "localizations",
            extensionName = "properties"
        )

        file.bufferedWriter().use { writer ->
            for (key in localizations.distinct()) {
                writer.write("$key=\n")
            }
        }
    }
}