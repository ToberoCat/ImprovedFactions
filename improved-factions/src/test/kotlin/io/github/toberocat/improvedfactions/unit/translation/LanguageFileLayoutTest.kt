package io.github.toberocat.improvedfactions.unit.translation

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LanguageFileLayoutTest {
    @Test
    fun `bundled language files use language codes without a messages prefix`() {
        val languageDirectory = java.io.File(
            checkNotNull(javaClass.getResource("/languages")).toURI()
        )
        val languages = checkNotNull(languageDirectory.list()).toSet()

        assertEquals(setOf("en_us.properties", "es.properties"), languages)
    }
}
