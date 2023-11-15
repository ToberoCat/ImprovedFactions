package io.github.toberocat.improvedfactions.translation

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.Locale

object TranslatedBossBars {
    private val bossBarMap: MutableMap<TranslatedBossBarsType, MutableMap<Locale, BossBar>> = mutableMapOf()
    fun Player.getBossBar(type: TranslatedBossBarsType, constructor: (name: Component) -> BossBar): BossBar {
        val locale = getLocaleEnum()
        return bossBarMap.getOrPut(type) { mutableMapOf() }.computeIfAbsent(locale) {
            return@computeIfAbsent constructor(
                it.localize("base.boss-bars.$type", emptyMap())
            )
        }
    }
}


enum class TranslatedBossBarsType {
    RAIDABLE;

    override fun toString() = name.lowercase().replace("_", "-")
}
