package io.github.toberocat.improvedfactions.localization

import com.google.devtools.ksp.processing.KSPLogger
import java.io.File
import java.util.*

class LocalizationReader(private val languageFolder: File, private val logger: KSPLogger) {
    private val properties = getPropertiesFile()

    fun getLocalization(key: String): String {
        return properties.getProperty(key, key).replace("|", "\\|")
    }

    fun getExistingLocalizationKeys() = properties.keys.map { it.toString() }

    private fun getPropertiesFile(): Properties {
        val propertiesFileName = "messages_en.properties"
        val properties = Properties()
        val file = File(languageFolder, propertiesFileName)
        if (!file.exists()) {
            logger.error("Localization file $propertiesFileName not found.")
            return properties
        }

        file.inputStream().use { properties.load(it) }
        return properties
    }
}