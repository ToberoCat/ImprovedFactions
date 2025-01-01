package io.github.toberocat.improvedfactions.permission.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.toberocat.improvedfactions.annotations.permission.Permission
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.permission.generator.PermissionDocumentationGenerator
import io.github.toberocat.improvedfactions.permission.generator.PermissionPluginYmlGenerator
import io.github.toberocat.improvedfactions.permission.visitor.PermissionVisitor
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class PermissionProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val pluginYmlFile: File
) : SymbolProcessor {
    private val permissions = mutableListOf<Permission>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Permission::class.qualifiedName!!)
        if (!symbols.iterator().hasNext()) {
            return emptyList()
        }

        logger.info("Processing permission symbols...")
        val visitor = PermissionVisitor()
        symbols.forEach { it.accept(visitor, Unit) }
        permissions.addAll(visitor.permissions)

        return emptyList()
    }

    override fun finish() {
        PermissionPluginYmlGenerator(logger, permissions).appendYmlToPluginYml(pluginYmlFile)

        // Add factions.* permission for documentation generation
        permissions.add(Permission("factions.*", PermissionConfigurations.OP_ONLY))
        PermissionDocumentationGenerator(logger, codeGenerator, permissions).createPermissionsMd()
    }
}