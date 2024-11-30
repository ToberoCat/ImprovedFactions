package io.github.toberocat.improvedfactions.factions

import io.github.toberocat.improvedfactions.translation.LocalizationKey


data class LocalizedMessage(val key: LocalizationKey, val placeholders: Map<String, String>)