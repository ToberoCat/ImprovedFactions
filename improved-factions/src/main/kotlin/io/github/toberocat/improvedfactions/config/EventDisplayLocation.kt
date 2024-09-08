package io.github.toberocat.improvedfactions.config

import io.github.toberocat.improvedfactions.translation.LocalizationKey
import io.github.toberocat.improvedfactions.translation.getLocalized
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.toAudience
import net.kyori.adventure.title.TitlePart
import org.bukkit.entity.Player

enum class EventDisplayLocation {
    TITLE {
        override fun display(
            player: Player,
            mainMessage: LocalizationKey,
            subMessage: LocalizationKey?,
            placeholders: Map<String, String>
        ) {
            val audience = player.toAudience()
            audience.sendTitlePart(TitlePart.TITLE, player.getLocalized(mainMessage, placeholders))
            subMessage?.let { audience.sendTitlePart(TitlePart.SUBTITLE, player.getLocalized(it, placeholders)) }
        }

    },
    ACTIONBAR {
        override fun display(
            player: Player,
            mainMessage: LocalizationKey,
            subMessage: LocalizationKey?,
            placeholders: Map<String, String>
        ) {
            val audience = player.toAudience()
            audience.sendActionBar(player.getLocalized(mainMessage, placeholders))
            subMessage?.let { player.sendLocalized(it, placeholders) }
        }
    },
    CHAT {
        override fun display(
            player: Player,
            mainMessage: LocalizationKey,
            subMessage: LocalizationKey?,
            placeholders: Map<String, String>
        ) {
            player.sendLocalized(mainMessage, placeholders)
            subMessage?.let { player.sendLocalized(subMessage, placeholders) }
        }
    };

    abstract fun display(
        player: Player,
        mainMessage: LocalizationKey,
        subMessage: LocalizationKey?,
        placeholders: Map<String, String> = emptyMap()
    )
}