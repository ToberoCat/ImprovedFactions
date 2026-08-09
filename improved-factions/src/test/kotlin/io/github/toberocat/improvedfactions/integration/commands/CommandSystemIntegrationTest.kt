package io.github.toberocat.improvedfactions.integration.commands

import io.github.toberocat.improvedfactions.FactionsIntegrationTest
import io.github.toberocat.improvedfactions.commands.processor.generatedCommandContracts
import io.github.toberocat.improvedfactions.database.storage.ClaimKey
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CommandSystemIntegrationTest : FactionsIntegrationTest() {
    @Test
    fun `active generated contracts are registered in the real executor`() {
        val activeModules = plugin.moduleManager.activeModules.keys
        val expected = generatedCommandContracts
            .filter { it.module in activeModules }
            .map { it.label }
            .toSet()

        assertEquals(expected, plugin.executor.commandProcessors.keys)
        plugin.executor.commandProcessors.forEach { (label, processor) ->
            assertEquals(label, processor.contract.label)
            assertEquals(generatedCommandContracts.single { it.label == label }, processor.contract)
        }
    }

    @Test
    fun `executor enforces generated permission before parsing arguments`() {
        val owner = player("Owner")

        command("/f ban Nobody")
            .asPlayer(owner)
            .withoutPermission()
            .run()
            .expectHandled(false)
            .expectLocalizedResponse("base.commands.cant-execute")
    }

    @Test
    fun `executor routes invalid arguments to generated localized response`() {
        val owner = player("Owner")

        command("/f ban")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .expectDeclaredResponse("ban", "missingRequiredArgument")
    }

    @Test
    fun `camel case declared labels remain reachable case insensitively`() {
        val admin = player("Admin")

        command("/f admin playerinfo")
            .asPlayer(admin)
            .run()
            .expectHandled()
            .expectDeclaredResponse("admin playerInfo", "missingRequiredArgument")
    }

    @Test
    fun `home command reaches generated player route`() {
        val player = player("Homeless")

        command("/f home")
            .asPlayer(player)
            .run()
            .expectHandled()
            .expectDeclaredResponse("home", "notInFaction")
    }

    @Test
    fun `aliases use executor tab completion`() {
        val owner = player("Owner")
        player("Target")

        command("/fac inv")
            .asPlayer(owner)
            .complete()
            .expectContains("invite", "invites", "inviteaccept", "invitediscard")

        command("/f invite Tar")
            .asPlayer(owner)
            .complete()
            .expectContains("Target")
    }

    @Test
    fun `claim command persists state through Bukkit dispatch`() {
        val owner = player("ClaimOwner")
        faction(owner.uniqueId)
        val world = owner.world
        BaseModule.config.allowedWorlds += world.name
        val key = ClaimKey(world.name, owner.location.chunk.x, owner.location.chunk.z)

        command("/f claim").asPlayer(owner).run().expectHandled().awaitStorage()

        assertNotNull(StorageManager.cache.claim(key))
    }

    @Test
    fun `invite command persists state through Bukkit dispatch`() {
        val owner = player("InviteOwner")
        val target = player("InviteTarget")
        faction(owner.uniqueId)

        command("/f invite ${target.name} Member")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .awaitStorage()

        assertTrue(StorageManager.cache.invites(target.uniqueId).isNotEmpty())
    }
}
