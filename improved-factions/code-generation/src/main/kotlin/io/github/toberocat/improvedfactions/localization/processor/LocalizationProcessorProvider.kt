package io.github.toberocat.improvedfactions.localization.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import java.io.File

class LocalizationProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val languageFolder = File(environment.options["languageFolder"]!!)
        return LocalizationProcessor( environment.logger, languageFolder)
    }
}