package io.github.toberocat.improvedfactions.utils

import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URISyntaxException
import java.nio.file.*
import java.util.function.Consumer

/**
 * FileUtils is a utility class providing file-related operations for Java plugins.
 *
 *
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
object FileUtils {
    /**
     * Copies all files and directories from the specified root folder within the plugin's JAR file to the plugin's data folder.
     * If a folder is encountered during the copying process, it recursively copies its contents as well.
     *
     * @param plugin The JavaPlugin instance of the plugin.
     * @param root   The root folder within the JAR file to copy from.
     * @return The File representing the root folder in the plugin's data folder after copying.
     * @throws IOException        If an I/O error occurs during copying.
     * @throws URISyntaxException If the plugin's root URL cannot be converted to a URI.
     */
    @Throws(IOException::class, URISyntaxException::class)
    fun copyAll(plugin: JavaPlugin, root: String): File {
        plugin.dataFolder.mkdirs()
        foreachIn(plugin, root) { x: Path ->
            if (x.toString() == root) return@foreachIn
            try {
                val file = root + "/" + x.fileName
                if (Files.isDirectory(x)) {
                    copyAll(plugin, file)
                } else {
                    copyResource(plugin, file)
                }
            } catch (e: IOException) {
                val sw = StringWriter()
                e.printStackTrace(PrintWriter(sw))
                val stackTrace = sw.toString()
                plugin.logger.severe(
                    """
    An exception occurred: ${e.message}
    $stackTrace
    """.trimIndent()
                )
            } catch (e: URISyntaxException) {
                val sw = StringWriter()
                e.printStackTrace(PrintWriter(sw))
                val stackTrace = sw.toString()
                plugin.logger.severe(
                    """
    An exception occurred: ${e.message}
    $stackTrace
    """.trimIndent()
                )
            }
        }
        return File(plugin.dataFolder, root)
    }

    /**
     * Lists all files and directories within the specified root folder within the plugin's JAR file.
     *
     * @param plugin The JavaPlugin instance of the plugin.
     * @param root   The root folder within the JAR file to list from.
     * @throws URISyntaxException If the plugin's root URL cannot be converted to a URI.
     * @throws IOException        If an I/O error occurs while listing the files and directories.
     */
    @Throws(URISyntaxException::class, IOException::class)
    fun foreachIn(plugin: JavaPlugin, root: String, consumer: Consumer<Path>) {
        val url = plugin.javaClass.getResource("/$root") ?: return
        val uri = url.toURI()
        if ("jar" != uri.scheme) {
            walk(Paths.get(uri), consumer)
            return
        }
        try {
            FileSystems.newFileSystem(uri, emptyMap<String, Any>()).use { fileSystem ->
                walk(
                    fileSystem.getPath(
                        "/$root"
                    ), consumer
                )
            }
        } catch (e: FileSystemAlreadyExistsException) {
            val fileSystem = FileSystems.getFileSystem(uri)
            walk(fileSystem.getPath("/$root"), consumer)
        }
    }

    @Throws(IOException::class)
    private fun walk(myPath: Path, consumer: Consumer<Path>) {
        val walk = Files.walk(myPath, 1)
        val it = walk.iterator()
        while (it.hasNext()) consumer.accept(it.next())
        walk.close()
    }

    /**
     * Copies a resource file from the plugin's JAR file to the plugin's data folder.
     *
     * @param plugin The JavaPlugin instance of the plugin.
     * @param res    The resource file path within the JAR file to copy.
     * @throws IOException If an I/O error occurs during copying.
     */
    @Throws(IOException::class)
    fun copyResource(plugin: JavaPlugin, res: String) {
        val src = plugin.javaClass.getResourceAsStream("/$res") ?: return
        val file = File(plugin.dataFolder, res)
        if (!file.exists()) file.mkdirs() else return
        Files.copy(src, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}