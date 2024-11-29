package io.github.toberocat.improvedfactions.commands.arguments

import io.github.toberocat.improvedfactions.translation.LocalizedException

class ArgumentParsingException(key: String, placeholders: Map<String, String> = emptyMap()) : LocalizedException(key, placeholders)