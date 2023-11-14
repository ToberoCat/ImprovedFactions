package io.github.toberocat.improvedfactions.translation

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
class ExternalResourceBundleLoader(private val bundlePath: String) : ResourceBundle.Control() {
    @Throws(IOException::class)
    override fun newBundle(
        baseName: String,
        locale: Locale,
        format: String,
        loader: ClassLoader,
        reload: Boolean
    ): ResourceBundle {
        val resourceName = toResourceName(toBundleName(baseName, locale), "properties")
        val file = File(bundlePath, resourceName)
        if (file.exists()) {
            FileInputStream(file).use { stream -> return PropertyResourceBundle(stream) }
        }
        return super.newBundle(baseName, locale, format, loader, reload);
    }

    override fun getFallbackLocale(baseName: String?, locale: Locale?): Locale = Locale.ENGLISH
}