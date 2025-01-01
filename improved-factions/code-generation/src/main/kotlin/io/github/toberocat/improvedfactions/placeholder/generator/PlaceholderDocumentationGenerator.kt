package io.github.toberocat.improvedfactions.placeholder.generator

import com.google.devtools.ksp.processing.CodeGenerator
import io.github.toberocat.improvedfactions.annotations.papi.PapiPlaceholder
import io.github.toberocat.improvedfactions.utils.writeDocumentation

class PlaceholderDocumentationGenerator(
    private val codeGenerator: CodeGenerator,
    private val placeholders: MutableList<PapiPlaceholder>
) {
    fun createPlaceholdersMd() {
        codeGenerator.writeDocumentation(
            file = "placeholders",
            tags = arrayOf("Papi", "Placeholders")
        ) { writer ->
            writer.write("# Placeholders\n\n")
            writer.write("This file contains all placeholders that are available in ImprovedFactions.\n")
            writer.write("They can be used with `Papi`\n\n")

            writer.write("| Placeholder | Module | Description |\n")
            writer.write("| --- | --- | --- |\n")
            placeholders.forEach { placeholder ->
                writer.write("| `%faction_${placeholder.value}%` | ${placeholder.module} | ${placeholder.description} |\n")
            }
        }
    }
}