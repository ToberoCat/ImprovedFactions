package io.github.toberocat.improvedfactions.unit.commands

import io.github.toberocat.improvedfactions.commands.processor.generatedCommandContracts
import io.github.toberocat.improvedfactions.testing.UnitTest
import org.junit.jupiter.api.Test
import org.yaml.snakeyaml.Yaml
import java.util.Properties
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@UnitTest
class GeneratedCommandContractTest {
    @Test
    fun `generated contracts have unique consistent routes`() {
        assertTrue(generatedCommandContracts.isNotEmpty())
        assertEquals(
            generatedCommandContracts.size,
            generatedCommandContracts.map { it.label.lowercase() }.distinct().size,
            "Generated command labels must be unique",
        )

        generatedCommandContracts.forEach { command ->
            assertTrue(command.label.matches(Regex("[A-Za-z0-9]+(?: [A-Za-z0-9]+)*")), command.label)
            assertTrue(command.routes.isNotEmpty(), "${command.label} has no executable route")
            assertTrue(command.responses.isNotEmpty(), "${command.label} has no declared/default responses")
            assertEquals(command.responses.size, command.responses.map { it.name }.distinct().size, command.label)
            command.routes.forEach { route ->
                assertEquals(route.arguments.indices.toList(), route.arguments.map { it.index }, command.label)
            }
        }
    }

    @Test
    fun `every generated localization key exists in canonical locale`() {
        val requiredKeys = generatedCommandContracts.flatMap { command ->
            buildList {
                add(command.categoryLocalizationKey)
                add(command.descriptionLocalizationKey)
                addAll(command.responses.map { it.localizationKey })
                command.routes.flatMapTo(this) { route ->
                    route.arguments.flatMap { listOf(it.usageLocalizationKey, it.descriptionLocalizationKey) }
                }
            }
        }.toSet()

        val properties = Properties().apply {
            checkNotNull(this@GeneratedCommandContractTest.javaClass.getResourceAsStream("/languages/en_us.properties"))
                .use(::load)
        }
        val missing = requiredKeys.filterNot(properties::containsKey)
        assertTrue(missing.isEmpty(), "en_us.properties misses generated command keys: $missing")
    }

    @Test
    fun `generated permissions match plugin declarations and defaults`() {
        @Suppress("UNCHECKED_CAST")
        val plugin = checkNotNull(javaClass.getResourceAsStream("/plugin.yml")).use {
            Yaml().load<Map<String, Any>>(it)
        }
        @Suppress("UNCHECKED_CAST")
        val permissions = plugin["permissions"] as Map<String, Map<String, Any>>

        generatedCommandContracts.forEach { command ->
            val declaration = permissions[command.permission.node]
            assertTrue(declaration != null, "${command.label}: ${command.permission.node} is absent from plugin.yml")
            assertEquals(command.permission.default.ymlValue, declaration["default"].toString(), command.label)
        }
    }
}
