package io.github.toberocat.improvedfactions.integration

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.translation.localizeUnformatted
import org.junit.jupiter.api.Test
import java.util.Locale
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ClaimPowerLocalizationTest : ImprovedFactionsTest() {
    @Test
    fun `not enough power for claim message is available in the English locale`() {
        val key = "base.exceptions.not-enough-power-for-claim"
        val message = Locale.ENGLISH.localizeUnformatted(key, mapOf("x" to "0", "z" to "0"))

        assertNotEquals(key, message)
        assertTrue(message.contains("0, 0"))
    }

    @Test
    fun `language-only Spanish file is used as a fallback for regional Spanish locales`() {
        val message = Locale("es", "MX").localizeUnformatted("base.exception.command-not-found", emptyMap())

        assertTrue(message.contains("Comando no encontrado"))
    }
}
