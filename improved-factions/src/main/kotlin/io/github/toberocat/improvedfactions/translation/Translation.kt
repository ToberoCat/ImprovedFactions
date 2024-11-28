package io.github.toberocat.improvedfactions.translation

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.utils.toAudience
import net.kyori.adventure.audience.Audience.toAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.ComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import sun.security.util.LocalizedMessage.getLocalized
import java.util.*


/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
typealias LocalizationKey = String

fun CommandSender.resolveLocalization(key: String, placeholders: Map<String, String> = emptyMap()) = when (this) {
    is Player -> getLocaleEnum().localizeUnformatted(key, placeholders)
    else -> Locale.ENGLISH.localizeUnformatted(key, placeholders)
}

fun CommandSender.sendLocalized(key: String, placeholders: Map<String, String> = emptyMap()) {
    when (this) {
        is Player -> sendLocalized(key, placeholders)
        else -> sendMessage(Locale.ENGLISH.localizeUnformatted(key, placeholders))
    }
}

fun OfflinePlayer.sendLocalized(key: String, placeholders: Map<String, String> = emptyMap()) {
    player?.sendLocalized(key, placeholders)
    // ToDo: Make sure players can catch up on messages when they log in
}

fun Player.sendLocalized(key: String, placeholders: Map<String, String> = emptyMap()) =
    toAudience().sendMessage(getLocalized(key, placeholders))

fun Player.getLocalized(key: String, placeholders: Map<String, String> = emptyMap()) =
    getLocaleEnum().localize(key, placeholders)

fun Player.getLocaleEnum(): Locale {
    val localeParts = locale.split("_")
    val locale = when (localeParts.size) {
        1 -> Locale(localeParts[0])
        2 -> Locale(localeParts[0], localeParts[1])
        3 -> Locale(localeParts[0], localeParts[1], localeParts[2])
        else -> Locale.ENGLISH
    }
    return locale
}

fun Player.getUnformattedLocalized(key: String, placeholders: Map<String, String> = emptyMap()) =
    getLocaleEnum().localizeUnformatted(key, placeholders)

fun Locale.localize(key: LocalizationKey, placeholders: Map<String, String>): Component =
    MiniMessage.miniMessage().deserialize(localizeUnformatted(key, placeholders))

fun Locale.localizeUnformatted(key: LocalizationKey, placeholders: Map<String, String>): String {
    val bundle = getBundle()
    if (!bundle.containsKey(key)) {
        ImprovedFactionsPlugin.instance.logger.warning("Missing $key in the language file for $this")
        return key
    }
    val localizedString = bundle.getString(key)
    val mutablePlaceholders = placeholders.toMutableMap()
    if (bundle.containsKey("base.prefix")) mutablePlaceholders["prefix"] = bundle.getString("base.prefix")

    fun replacePlaceholders(str: String, placeholders: Map<String, String>): String {
        var result = str
        var changed = false

        for ((placeholder, value) in placeholders) {
            val placeholderPattern = Regex("\\{\\s*${Regex.escape(placeholder)}\\s*\\}")
            val replaced = result.replace(
                placeholderPattern, MiniMessage.miniMessage().serialize(
                    LegacyComponentSerializer
                        .legacyAmpersand()
                        .deserialize(
                            value.replace(
                                LegacyComponentSerializer.SECTION_CHAR.toString(),
                                LegacyComponentSerializer.AMPERSAND_CHAR.toString()
                            )
                        )
                )
            )
            if (replaced == result) continue
            result = replaced
            changed = true
        }

        return if (changed) replacePlaceholders(result, placeholders) else result
    }
    return replacePlaceholders(localizedString, mutablePlaceholders)
}

fun Locale.getBundle(): ResourceBundle = ResourceBundle.getBundle(
    "languages.messages", this, ExternalResourceBundleLoader(ImprovedFactionsPlugin.instance.dataFolder.absolutePath)
)