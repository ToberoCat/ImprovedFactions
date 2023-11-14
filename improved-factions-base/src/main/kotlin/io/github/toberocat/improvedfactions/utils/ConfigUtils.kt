package io.github.toberocat.improvedfactions.utils

import org.bukkit.configuration.ConfigurationSection
import kotlin.math.abs

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
inline fun <reified T : Enum<T>> ConfigurationSection.getEnum(path: String): T? = getString(path).getEnum<T>()

inline fun <reified T : Enum<T>> String?.getEnum(): T? {
    return enumValueOf<T>(this ?: return null)
}

fun ConfigurationSection.getUnsignedDouble(path: String, default: Double) = abs(getDouble(path, default))