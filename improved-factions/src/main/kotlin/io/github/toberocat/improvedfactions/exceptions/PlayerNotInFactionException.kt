package io.github.toberocat.improvedfactions.exceptions

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.translation.LocalizedException

@Localization("base.exceptions.player-not-in-faction")
class PlayerNotInFactionException : LocalizedException("base.exceptions.player-not-in-faction", emptyMap())
