package io.github.toberocat.improvedfactions.testing

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.commands.processor.generatedCommandContractsByLabel
import io.github.toberocat.improvedfactions.translation.getLocalized
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.command.CommandSender
import org.mockbukkit.mockbukkit.command.MessageTarget
import org.mockbukkit.mockbukkit.entity.PlayerMock
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Fluent entry point that always crosses Bukkit's command boundary. */
class CommandTestInvocation internal constructor(
    private val fixture: FactionsIntegrationTest,
    input: String,
) {
    private val commandLine = input.removePrefix("/").trim()
    private var sender: CommandSender? = null

    init {
        require(commandLine.isNotBlank()) { "command must not be blank" }
    }

    fun asPlayer(player: PlayerMock) = apply { sender = player }

    fun withLocale(locale: Locale) = apply {
        checkNotNull(sender as? PlayerMock) { "Call asPlayer before withLocale" }.setLocale(locale)
    }

    fun asConsole() = apply { sender = fixture.server.consoleSender }

    /** Denies the generated Bukkit permission for the route in this invocation. */
    fun withoutPermission() = apply {
        val player = checkNotNull(sender as? PlayerMock) { "Call asPlayer before withoutPermission" }
        val contract = checkNotNull(resolveContract()) { "No generated contract matches '$commandLine'" }
        player.isOp = false
        player.addAttachment(fixture.plugin, contract.permission.node, false)
    }

    fun run(): CommandTestOutcome {
        val actor = checkNotNull(sender) { "Choose asPlayer or asConsole before run" }
        return CommandTestOutcome(
            fixture = fixture,
            sender = actor,
            handled = fixture.server.dispatchCommand(actor, commandLine),
        )
    }

    fun complete(): CommandCompletionOutcome {
        val actor = checkNotNull(sender) { "Choose asPlayer or asConsole before complete" }
        return CommandCompletionOutcome(fixture.server.getCommandTabComplete(actor, commandLine))
    }

    private fun resolveContract() = commandArguments()
        .let { args ->
            generatedCommandContractsByLabel.values
                .sortedByDescending { it.label.length }
                .firstOrNull {
                    args.equals(it.label, ignoreCase = true) || args.startsWith("${it.label} ", ignoreCase = true)
                }
        }

    private fun commandArguments(): String {
        val root = commandLine.substringBefore(' ')
        return if (root.equals("f", true) || root.equals("fac", true) ||
            root.equals("faction", true) || root.equals("factions", true)
        ) commandLine.substringAfter(' ', "") else commandLine
    }
}

class CommandTestOutcome internal constructor(
    private val fixture: FactionsIntegrationTest,
    private val sender: CommandSender,
    val handled: Boolean,
) {
    fun expectHandled(expected: Boolean = true) = apply { assertEquals(expected, handled) }

    fun awaitStorage() = apply { fixture.awaitStorage() }

    fun expectLocalizedResponse(
        localizationKey: String,
        placeholders: Map<String, String> = emptyMap(),
    ) = apply {
        val player = checkNotNull(sender as? PlayerMock) {
            "Exact component localization assertions currently require a PlayerMock"
        }
        val plainText = PlainTextComponentSerializer.plainText()
        assertEquals(
            plainText.serialize(player.getLocalized(localizationKey, placeholders)),
            plainText.serialize(checkNotNull(player.nextComponentMessage())),
        )
    }

    fun expectDeclaredResponse(
        commandLabel: String,
        responseName: String,
        placeholders: Map<String, String> = emptyMap(),
    ) = apply {
        val contract = checkNotNull(generatedCommandContractsByLabel[commandLabel]) {
            "No generated command contract for '$commandLabel'"
        }
        val response = checkNotNull(contract.responses.find { it.name == responseName }) {
            "Command '$commandLabel' does not declare response '$responseName'"
        }
        expectLocalizedResponse(response.localizationKey, placeholders)
    }

    fun expectMessageContaining(fragment: String) = apply {
        val target = checkNotNull(sender as? MessageTarget) { "Sender does not expose MockBukkit messages" }
        assertTrue(checkNotNull(target.nextMessage()).contains(fragment, ignoreCase = true))
    }
}

class CommandCompletionOutcome internal constructor(val suggestions: List<String>) {
    fun expectContains(vararg expected: String) = apply {
        expected.forEach { suggestion ->
            assertTrue(
                suggestions.any { it.equals(suggestion, ignoreCase = true) },
                "Expected completion '$suggestion', got $suggestions",
            )
        }
    }
}
