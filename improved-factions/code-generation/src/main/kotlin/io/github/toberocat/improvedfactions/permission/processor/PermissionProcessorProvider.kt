package io.github.toberocat.improvedfactions.permission.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import java.io.File

class PermissionProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val pluginYmlFile = File(environment.options["plugin.yml"]!!)
        return PermissionProcessor(environment.codeGenerator, environment.logger, pluginYmlFile)
    }
}