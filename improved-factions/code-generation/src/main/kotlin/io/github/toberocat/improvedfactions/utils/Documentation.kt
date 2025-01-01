package io.github.toberocat.improvedfactions.utils

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import java.io.BufferedWriter

fun CodeGenerator.writeDocumentation(
    file: String,
    title: String? = null,
    tags: Array<String>? = null,
    description: String? = null,
    writerCb: (BufferedWriter) -> Unit
) {
    val fileName = file.split("/").last()
    val subDirectory = file.removeSuffix(fileName).replace("/", ".").removeSuffix(".")
    val outputStream = createNewFile(
        dependencies = Dependencies.ALL_FILES,
        packageName = "docs.${subDirectory}",
        fileName = fileName,
        extensionName = "md"
    )

    outputStream.bufferedWriter().use { writer ->
        writer.write("---\n")
        title?.let { writer.write("title: ${it}\n") }
        description?.let { writer.write("description: ${it}\n") }
        tags?.let { writer.write("keywords: ${it.joinToString(", ", "[", "]")}\n") }
        tags?.let { writer.write("tags: ${it.joinToString(", ", "[", "]")}\n") }
        writer.write("---\n\n")
        writerCb(writer)
    }
}