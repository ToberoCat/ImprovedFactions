package io.github.toberocat.improvedfactions.unit.factions

import io.github.toberocat.improvedfactions.factions.defaultFactionRanks
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class DefaultFactionRanksTest {
    @Test
    fun `fallback member rank can use faction home`() {
        val member = defaultFactionRanks(null).first { it.name == "Member" }

        assertTrue(Permissions.HOME in member.allowedPermissions)
    }
}
