package io.github.toberocat.improvedfactions.integration

import io.github.toberocat.improvedfactions.ImprovedFactionsTest
import io.github.toberocat.improvedfactions.translation.sendLocalized
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TranslationIntegrationTest : ImprovedFactionsTest() {
    @Test
    fun `console localization renders MiniMessage instead of printing tags`() {
        server.consoleSender.sendLocalized("base.commands.unknown-command")

        val component = assertNotNull(server.consoleSender.nextComponentMessage())
        val plainText = PlainTextComponentSerializer.plainText().serialize(component)

        assertTrue(plainText.contains("Unknown command"))
        assertFalse(plainText.contains("<red>"))
        assertFalse(plainText.contains("</red>"))
    }
}
