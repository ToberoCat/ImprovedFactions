package io.github.toberocat.improvedfactions.commands

import io.github.toberocat.improvedfactions.annotations.LocalizationKey

data class CommandProcessResult(
    val responseLocalizationKey: LocalizationKey,
    val args: Map<String, String>,
)