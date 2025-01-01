package io.github.toberocat.improvedfactions.localization.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.localization.LocalizationReader
import io.github.toberocat.improvedfactions.localization.visitor.LocalizationVisitor
import java.io.File
import java.io.InputStreamReader
import java.util.*

class LocalizationProcessor(
    private val logger: KSPLogger,
    languageFolder: File
) : SymbolProcessor {

    private val localizations = mutableListOf<String>()
    private val localizationReader = LocalizationReader(languageFolder, logger)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Localization::class.qualifiedName!!)
        if (!symbols.iterator().hasNext()) {
            return emptyList()
        }

        logger.info("Processing localization symbols...")

        val visitor = LocalizationVisitor(logger, resolver)
        symbols.forEach { it.accept(visitor, Unit) }
        localizations.addAll(visitor.localizations)

        return emptyList()
    }

    override fun finish() {
        val existingKeys = localizationReader.getExistingLocalizationKeys()
        val missingKeys = localizations.distinct().filter { it !in existingKeys }
        val possibleUnnecessaryKeys = existingKeys.filter { it !in localizations }

        if (possibleUnnecessaryKeys.isNotEmpty()) {
            logger.warn("""Possible unnecessary localization keys: 
                |${possibleUnnecessaryKeys.joinToString("\n")}
                |""".trimMargin())
        }

        if (missingKeys.isNotEmpty()) {
            logger.error("""Missing localization keys: 
                |${missingKeys.joinToString("\n")}
                |""".trimMargin())
        }
    }
}
