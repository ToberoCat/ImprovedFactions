package io.github.toberocat.improvedfactions.unit.factions

import io.github.toberocat.improvedfactions.factions.defaultFactionRanks
import io.github.toberocat.improvedfactions.factions.requiredDefaultFactionRanks
import io.github.toberocat.improvedfactions.permissions.Permissions
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class DefaultFactionRanksTest {
    @Test
    fun `configured member rank can use faction home`() {
        val config = YamlConfiguration().apply {
            set("factions.default-faction-ranks.Member.priority", 1)
            set("factions.default-faction-ranks.Member.default-permissions", listOf(Permissions.HOME))
        }
        val member = defaultFactionRanks(requiredDefaultFactionRanks(config)).single()

        assertTrue(Permissions.HOME in member.allowedPermissions)
    }
}
