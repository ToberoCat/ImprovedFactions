package io.github.toberocat.improvedfactions.permission.generator

import com.google.devtools.ksp.processing.KSPLogger
import io.github.toberocat.improvedfactions.annotations.permission.Permission
import java.io.File
import java.nio.file.Files

class PermissionPluginYmlGenerator(
    private val logger: KSPLogger,
    private val permissions: List<Permission>
) {

    fun appendYmlToPluginYml(pluginYmlFile: File) {
        if (permissions.isEmpty()) {
            logger.info("No permissions to generate.")
            return
        }

        try {
            val yamlContent = buildYaml(permissions)

            if (!Files.exists(pluginYmlFile.toPath())) {
                logger.error("plugin.yml file does not exist at $pluginYmlFile.")
                return
            }

            val existingContent = Files.readAllLines(pluginYmlFile.toPath(), Charsets.UTF_8).toMutableList()

            val permissionsStartIndex = existingContent.indexOfFirst { it.trim().startsWith("permissions:") }
            if (permissionsStartIndex != -1) {
                val permissionsEndIndex = existingContent
                    .indexOfFirst { it.trim().isEmpty() && existingContent.indexOf(it) > permissionsStartIndex }
                    .takeIf { it != -1 } ?: existingContent.size

                existingContent.subList(permissionsStartIndex, permissionsEndIndex).clear()
                logger.info("Replaced existing permissions block in plugin.yml.")
            }

            existingContent.addAll(yamlContent.lines())
            Files.write(pluginYmlFile.toPath(), existingContent, Charsets.UTF_8)

            logger.info("Updated permissions in plugin.yml successfully.")
        } catch (e: Exception) {
            logger.error("Failed to update permissions in plugin.yml: ${e.message}")
        }
    }

    private fun buildYaml(permissions: List<Permission>): String {
        val root = PermissionNode("permissions")

        for (permission in permissions) {
            root.children.computeIfAbsent(permission.value) { PermissionNode(it) }.apply {
                default = permission.config.ymlValue
            }
        }

        root.children.computeIfAbsent("factions.*") { PermissionNode("factions.*") }.apply {
            default = "op"
            children += mutableMapOf(
                "children" to PermissionNode("children").apply {
                    permissions.forEach { permission ->
                        children.computeIfAbsent(permission.value) { PermissionNode(it) }.apply {
                            inlined = "true"
                        }
                    }
                },
            )
        }

        val builder = StringBuilder()
        serializeNode(root, builder, 0)
        return builder.toString()
    }

    private fun serializeNode(node: PermissionNode, builder: StringBuilder, indent: Int) {
        val indentStr = "  ".repeat(indent)
        builder.append("$indentStr${node.name}: ${node.inlined}\n")

        if (node.default != null) {
            val defaultIndentStr = "  ".repeat(indent + 1)
            builder.append("${defaultIndentStr}default: ${node.default}\n")
        }

        for (child in node.children.values) {
            serializeNode(child, builder, indent + 1)
        }
    }

    private class PermissionNode(val name: String) {
        val children: MutableMap<String, PermissionNode> = mutableMapOf()
        var default: String? = null
        var inlined: String = ""
    }
}