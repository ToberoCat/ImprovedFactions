package io.github.toberocat.improvedfactions.permission.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import io.github.toberocat.improvedfactions.utils.writeDocumentation
import io.github.toberocat.improvedfactions.permission.visitor.PermissionData

class PermissionDocumentationGenerator(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
    private val permissions: List<PermissionData>
) {

    fun createPermissionsMd() {
        if (permissions.isEmpty()) {
            logger.info("No permissions to generate.")
            return
        }

        val markdownContent = buildMarkdown(permissions)
        codeGenerator.writeDocumentation("permissions") { writer ->
            writer.write(markdownContent)
        }

        logger.info("Generated permissions.md successfully.")
    }

    private fun buildMarkdown(permissions: List<PermissionData>): String {
        val markdown = StringBuilder()
        markdown.appendLine("# Permissions")
        markdown.appendLine()
        markdown.appendLine("The following permissions are available in ImprovedFactions:")
        markdown.appendLine()
        markdown.appendLine("| Permission | Default |")
        markdown.appendLine("|------------|---------|")

        for (permission in permissions) {
            markdown.appendLine("| `${permission.value}` | ${permission.config.ymlValue} |")
        }

        return markdown.toString()
    }
}
