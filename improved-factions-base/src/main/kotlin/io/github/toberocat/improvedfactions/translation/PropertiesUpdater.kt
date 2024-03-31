package io.github.toberocat.improvedfactions.translation

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import java.io.File
import java.io.FileInputStream
import java.util.Properties

fun updateLanguages(plugin: ImprovedFactionsPlugin) {
    File(plugin.dataFolder, "languages").listFiles()?.forEach { file ->
        PropertiesUpdater("languages/messages_en.properties", file.absolutePath)
            .updatePropertiesFile()
            ?.let { plugin.logger.info("Updated language file ${file.nameWithoutExtension}. $it") }
    }
}

class PropertiesUpdater(private val resourceFilename: String, private val providedFilename: String) {

    fun updatePropertiesFile(): String? {
        val resourceProperties = loadPropertiesFromResource(resourceFilename)
        val providedProperties = loadPropertiesFromFile(providedFilename)

        val missingKeys = findMissingKeys(resourceProperties, providedProperties)

        if (missingKeys.isNotEmpty()) {
            addMissingKeys(providedProperties, missingKeys)
            savePropertiesToFile(providedFilename, providedProperties)
            return "Added ${missingKeys.size} missing keys to the properties file."
        }

        return null
    }

    private fun loadPropertiesFromResource(filename: String): Properties {
        val properties = Properties()
        val resourceStream = javaClass.classLoader.getResourceAsStream(filename)
        properties.load(resourceStream)
        return properties
    }

    private fun loadPropertiesFromFile(filename: String): Properties {
        val properties = Properties()
        val file = File(filename)
        if (file.exists()) {
            FileInputStream(file).use { properties.load(it) }
        }
        return properties
    }

    private fun findMissingKeys(resourceProps: Properties, providedProps: Properties): List<String> {
        val missingKeys = mutableListOf<String>()
        resourceProps.keys.forEach { key ->
            if (!providedProps.containsKey(key)) {
                missingKeys.add(key.toString())
            }
        }
        return missingKeys
    }

    private fun addMissingKeys(targetProps: Properties, missingKeys: List<String>) {
        missingKeys.forEach { key ->
            val value = javaClass.classLoader.getResourceAsStream(resourceFilename).use {
                Properties().apply { load(it) }.getProperty(key)
            }
            targetProps.setProperty(key, value)
        }
    }

    private fun savePropertiesToFile(filename: String, properties: Properties) {
        val file = File(filename)
        file.writer(Charsets.UTF_8).use {
            properties.store(it, "Auto-generated missing properties")
        }
    }
}
