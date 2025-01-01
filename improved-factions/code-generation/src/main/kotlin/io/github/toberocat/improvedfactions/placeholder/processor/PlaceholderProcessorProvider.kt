package io.github.toberocat.improvedfactions.placeholder.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import io.github.toberocat.improvedfactions.permission.processor.PermissionProcessor
import java.io.File

class PlaceholderProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return PlaceholderProcessor(environment.codeGenerator, environment.logger)
    }
}